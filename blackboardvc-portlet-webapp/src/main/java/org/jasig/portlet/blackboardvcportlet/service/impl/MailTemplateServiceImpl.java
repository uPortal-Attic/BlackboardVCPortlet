package org.jasig.portlet.blackboardvcportlet.service.impl;

import org.apache.velocity.app.VelocityEngine;
import org.jasig.portlet.blackboardvcportlet.service.MailTemplateService;
import org.jasig.portlet.blackboardvcportlet.service.util.MailMessages;
import org.jasig.portlet.blackboardvcportlet.service.util.MailTask;
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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service("jasigMailTemplateService")
public class MailTemplateServiceImpl implements BeanFactoryAware, MailTemplateService
{
	private static final Logger logger = LoggerFactory.getLogger(MailTemplateServiceImpl.class);

	final private Queue<MailTask> theQueue = new ConcurrentLinkedQueue<MailTask>();
	private JavaMailSender mailSender;
	private BeanFactory beanFactory;
	private VelocityEngine velocityEngine;

	@Value("mail.from")
	private String defaultFromAddress;

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

	private void sendMail(final MailTask mt)
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

	public void sendEmailUsingTemplate(String from, List<String> to, String subject, Map substitutions, MailMessages template)
	{
		theQueue.add(new MailTask(from, to, subject, substitutions, template));
	}

	public String buildEmailMessage(final MailTask mailTask)
	{
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, mailTask.getTemplate().getClassPathToTemplate(), "UTF-8", mailTask.getSubstitutions());
	}
}
