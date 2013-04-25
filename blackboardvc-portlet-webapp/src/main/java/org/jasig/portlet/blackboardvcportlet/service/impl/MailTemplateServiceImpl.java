package org.jasig.portlet.blackboardvcportlet.service.impl;

import org.apache.velocity.app.VelocityEngine;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.service.MailTask;
import org.jasig.portlet.blackboardvcportlet.service.MailTemplateService;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.jasig.portlet.blackboardvcportlet.service.util.MailMessages;
import org.jasig.portlet.blackboardvcportlet.service.util.MailSubstitutions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service("jasigMailTemplateService")
public class MailTemplateServiceImpl implements BeanFactoryAware, MailTemplateService
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

	final private Queue<MailTask> theQueue = new ConcurrentLinkedQueue<MailTask>();
	private JavaMailSender mailSender;
	@SuppressWarnings("unused")
	private BeanFactory beanFactory;
	private VelocityEngine velocityEngine;
	private SessionService sessionService;
	
	private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm";

	private String defaultFromAddress;
	
	@Value("mail.sendMail")
	private String sendMail;

	@Value("mail.extParticipantMailMessage.subject")
	private String externalParticipantSubject;
	@Value("mail.intParticipantMailMessage.subject")
	private String internalParticipantSubject;
	@Value("mail.moderatorMailMessage.subject")
	private String moderatorSubject;
	@Value("mail.sessionDeletionMessage.subject")
	private String sessionDeletionSubject;

	/**
	 * Default Constructor
	 */
	public MailTemplateServiceImpl()
	{
		super();
	}
	
	@Value("mail.from")
	public void setFrom(String from) {
		this.defaultFromAddress = from;
	}
	
	@Autowired
	public void setSessionService(SessionService service) {
		this.sessionService = service;
	}

	@Autowired
	public void setBeanFactory(BeanFactory bf) throws BeansException
	{
		beanFactory = bf;
	}

	@Autowired
	public void setJavaMailSender(JavaMailSender ms)
	{
		this.mailSender = ms;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine)
	{
		this.velocityEngine = velocityEngine;
	}

	/**
	 * Clear the queue every 1 second after last completion
	 */
	@Scheduled(fixedDelay = 1000)
	private void clearQueue()
	{
		MailTask cur = theQueue.poll();
		while (cur != null)
		{
			sendMail(cur);
			cur = theQueue.poll();
		}
	}

	public void sendMail(final MailTask mt)
	{
		try
		{
			MimeMessagePreparator messagePreparator = new MimeMessagePreparator()
			{
				public void prepare(MimeMessage mimeMessage) throws Exception
				{
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

					if (mt.getFrom() != null)
					{
						message.setFrom(mt.getFrom());
					}
					else
					{
						message.setFrom(defaultFromAddress);
					}
					if (mt.getTo() != null)
					{
						String[] toArray = mt.getTo().toArray(new String[mt.getTo().size()]);
						message.setTo(toArray);
					}
					if (mt.getSubject() != null)
					{
						message.setSubject(mt.getSubject());
					}
					else
					{
						switch (mt.getTemplate())
						{
							case MODERATOR:
								message.setSubject(moderatorSubject);
								break;
							case INTERNAL_PARTICIPANT:
								message.setSubject(internalParticipantSubject);
								break;
							case EXTERNAL_PARTICIPANT:
								message.setSubject(externalParticipantSubject);
								break;
							case SESSION_DELETION:
								message.setSubject(sessionDeletionSubject);
								break;
							default:
								message.setSubject("");
						}
					}

					message.setText(buildEmailMessage(mt), true);
				}
			};
			mailSender.send(messagePreparator);
		}
		catch (Exception e)
		{
			logger.error("Issue with sending email", e);
		}
	}

	@SuppressWarnings("unchecked")
	public String buildEmailMessage(final MailTask mailTask)
	{
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, mailTask.getTemplate().getClassPathToTemplate(), "UTF-8", mailTask.getSubstitutions());
	}

	@Override
	public void sendEmail(MailTask mailTask) {
		if("true".equalsIgnoreCase(sendMail))
			theQueue.add(mailTask);
	}
	
	public void buildAndSendNewSessionEmails(Session session) {
		
		for(ConferenceUser moderator : sessionService.getSessionChairs(session)) {
			this.sendEmail(buildModeratorMailTask(moderator, session));
		}
		
		for(ConferenceUser user : sessionService.getSessionNonChairs(session)) {
			this.sendEmail(buildParticipantMailTask(user, session));
		}
	}
	
	//build substitutions for moderator
	public MailTask buildModeratorMailTask(ConferenceUser moderator, Session session) {
		List<String> emailList = new ArrayList<String>();
		emailList.add(moderator.getEmail());
		
		//substitutions
		Map<String, String> substitutions = new HashMap<String, String>();
		
		substitutions.put(MailSubstitutions.DISPLAY_NAME.toString(), moderator.getDisplayName());
		substitutions.put(MailSubstitutions.SESSION_NAME.toString(), session.getSessionName());
		substitutions.put(MailSubstitutions.SESSION_TYPE.toString(), session.getAccessType().getName());
		substitutions.put(MailSubstitutions.SESSION_START_TIME.toString(), session.getStartTime().toString(DATE_FORMAT));
		substitutions.put(MailSubstitutions.SESSION_END_TIME.toString(), session.getEndTime().toString(DATE_FORMAT));
		substitutions.put(MailSubstitutions.SESSION_USER_URL.toString(), sessionService.getOrCreateSessionUrl(moderator, session));
		substitutions.put(MailSubstitutions.SESSION_GUEST_URL.toString(), session.getGuestUrl());
		
		MailTask mt = new MailTask(emailList,substitutions,MailMessages.MODERATOR);
		return mt;
	}
		
	//build substitutions for participant
	public MailTask buildParticipantMailTask(ConferenceUser participant, Session session) {
		List<String> emailList = new ArrayList<String>();
		emailList.add(participant.getEmail());
		
		//substitutions
		Map<String, String> substitutions = new HashMap<String, String>();
		
		substitutions.put(MailSubstitutions.DISPLAY_NAME.toString(), participant.getDisplayName());
		substitutions.put(MailSubstitutions.SESSION_NAME.toString(), session.getSessionName());
		substitutions.put(MailSubstitutions.SESSION_TYPE.toString(), session.getAccessType().getName());
		substitutions.put(MailSubstitutions.SESSION_START_TIME.toString(), session.getStartTime().toString(DATE_FORMAT));
		substitutions.put(MailSubstitutions.SESSION_END_TIME.toString(), session.getEndTime().toString(DATE_FORMAT));
		substitutions.put(MailSubstitutions.SESSION_USER_URL.toString(), sessionService.getOrCreateSessionUrl(participant, session));
		substitutions.put(MailSubstitutions.SESSION_GUEST_URL.toString(), session.getGuestUrl());
		substitutions.put(MailSubstitutions.SESSION_CREATOR_EMAIL.toString(), session.getCreator().getEmail());
		substitutions.put(MailSubstitutions.SESSION_CREATOR_NAME.toString(), session.getCreator().getDisplayName());
		
		MailTask mt = new MailTask(emailList,substitutions,MailMessages.EXTERNAL_PARTICIPANT);
		return mt;
	}
	
	public void buildAndSendCancelationMeetingEmail(Session session) {
		List <ConferenceUser> users = new ArrayList<ConferenceUser>();
		
		users.addAll(sessionService.getSessionChairs(session));
		users.addAll(sessionService.getSessionNonChairs(session));
		users.add(session.getCreator());
		
		for(ConferenceUser user : users) {
			this.sendEmail(buildCancellationNoticeMailTask(user, session));
		}
	}
		
	//build cancellation notice
	public MailTask buildCancellationNoticeMailTask(ConferenceUser user, Session session) {
		List<String> emailList = new ArrayList<String>();
		emailList.add(user.getEmail());
		
		//substitutions
		Map<String, String> substitutions = new HashMap<String, String>();
		
		substitutions.put(MailSubstitutions.DISPLAY_NAME.toString(), user.getDisplayName());
		substitutions.put(MailSubstitutions.SESSION_NAME.toString(), session.getSessionName());
		substitutions.put(MailSubstitutions.SESSION_TYPE.toString(), session.getAccessType().getName());
		substitutions.put(MailSubstitutions.SESSION_START_TIME.toString(), session.getStartTime().toString(DATE_FORMAT));
		substitutions.put(MailSubstitutions.SESSION_END_TIME.toString(), session.getEndTime().toString(DATE_FORMAT));
		substitutions.put(MailSubstitutions.SESSION_CREATOR_EMAIL.toString(), session.getCreator().getEmail());
		substitutions.put(MailSubstitutions.SESSION_CREATOR_NAME.toString(), session.getCreator().getDisplayName());
		
		MailTask mt = new MailTask(emailList,substitutions,MailMessages.SESSION_DELETION);
		return mt;
	}
}
