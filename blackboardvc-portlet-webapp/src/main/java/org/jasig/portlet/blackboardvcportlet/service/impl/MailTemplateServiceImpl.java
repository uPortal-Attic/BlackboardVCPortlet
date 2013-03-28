package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.mail.MessagingException;

import org.jasig.portlet.blackboardvcportlet.service.MailTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("jasigMailTemplateService")
public class MailTemplateServiceImpl implements BeanFactoryAware, MailTemplateService {
		private static final Logger logger = LoggerFactory.getLogger(MailTemplateServiceImpl.class);
		
		final private Queue<MailTask> theQueue = new ConcurrentLinkedQueue<MailTask>();

	    private JavaMailSender mailSender;
	    
	    private BeanFactory beanFactory;
	    
	    public MailTemplateServiceImpl () {
	    	super();
	    }

	    @Autowired
	    public void setBeanFactory(BeanFactory bf) throws BeansException {
	        beanFactory = bf;
	    }
	    
	    @Autowired
	    public void setJavaMailSender (JavaMailSender ms) {
	    	this.mailSender = ms;
	    }
	    
	    
	    private static class MailTask {
	        
		    private String from;
		    private List<String> to;
		    private String subject;
		    private Object[] substitutions;
		    private String template;
	
		    public MailTask(String from, List<String> to, String subject, String[] substitutions, String template) {
		        this.setFrom(from);
		        this.setTo(to);
		        this.setSubject(subject);
		        this.setSubstitutions(substitutions);
		        this.setTemplate(template);
		    }

			public String getFrom() {
				return from;
			}

			public void setFrom(String from) {
				this.from = from;
			}

			public List<String> getTo() {
				return to;
			}

			public void setTo(List<String> to) {
				this.to = to;
			}

			public String getSubject() {
				return subject;
			}

			public void setSubject(String subject) {
				this.subject = subject;
			}

			public Object[] getSubstitutions() {
				return substitutions;
			}

			public void setSubstitutions(Object[] substitutions) {
				this.substitutions = substitutions;
			}

			public String getTemplate() {
				return template;
			}

			public void setTemplate(String template) {
				this.template = template;
			}
	  }
	  
	  /**
	   * Clear the queue every 1 second after last completion
	   */
	  @Scheduled(fixedDelay=1000)
	  private void clearQueue() {
		  MailTask cur = theQueue.poll();
		  while(cur != null) {
			  sendMail(cur);
			  cur = theQueue.poll();
		  }
	  }
	  
	  private void sendMail(MailTask mt) {
	      try {
	            SimpleMailMessage message = new SimpleMailMessage((SimpleMailMessage)beanFactory.getBean(mt.getTemplate()));
	           
	            if (mt.getFrom() != null)
	            {
	                message.setFrom(mt.getFrom());
	            }
	            if (mt.getTo() != null)
	            {
	                String[] toArray = (String[]) mt.getTo().toArray(new String[mt.getTo().size()]);  
	                message.setTo(toArray);
	            }
	            if (mt.getSubject() != null)
	            {
	                message.setSubject(mt.getSubject());
	            }
	            
	            if (mt.getSubstitutions() != null)
	            {
	                message.setText(String.format(message.getText(), mt.getSubstitutions()));
	            }
	           
	            mailSender.send(message);
	                       
	      } catch (Exception e) {
	          logger.error("Issue with sending email",e);
	      }
	            
	    }
	  
	  
	  public void sendEmailUsingTemplate(String from, List<String> to, String subject, String[] substitutions, String template) throws MessagingException
	  {
	      theQueue.add(new MailTask(from,to,subject,substitutions,template));
	  }

}
