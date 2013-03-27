package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import javax.mail.MessagingException;

import org.jasig.portlet.blackboardvcportlet.service.MailTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("mailTemplateService")
public class MailTemplateServiceImpl implements BeanFactoryAware, MailTemplateService {
		private static final Logger logger = LoggerFactory.getLogger(MailTemplateServiceImpl.class);

	    @Autowired
	    private JavaMailSender mailSender;
	    
	    BeanFactory beanFactory;
	    
		private TaskExecutor taskExecutor;

		// Instantiate a task executor MailService
		public MailTemplateServiceImpl(TaskExecutor taskExecutor) {
			super();
			this.taskExecutor = taskExecutor;
		}
	    
	    public MailTemplateServiceImpl () {
	    	super();
	    }

	    @Override
	    public void setBeanFactory(BeanFactory bf) throws BeansException {
	        beanFactory = bf;
	    }
	    
	    // Runnable child class which allows the asynchronous sending of email
	    private class MailTask implements Runnable {
	        
	    private String from;
	    private List<String> to;
	    private String subject;
	    private Object[] substitutions;
	    private String template;

	    public MailTask(String from, List<String> to, String subject, String[] substitutions, String template) {
	        this.from=from;
	        this.to=to;
	        this.subject=subject;
	        this.substitutions=substitutions;
	        this.template=template;
	    }

	    /*
	     * The runnable method to send the email message
	     */
	        @Override
	    public void run() {
	      try
	      {
	            SimpleMailMessage message = new SimpleMailMessage((SimpleMailMessage)beanFactory.getBean(template));
	           
	            if (from!=null)
	            {
	                message.setFrom(from);
	            }
	            if (to!=null)
	            {
	                String[] toArray = (String[]) this.to.toArray(new String[this.to.size()]);  
	                message.setTo(toArray);
	            }
	            if (subject!=null)
	            {
	                message.setSubject(subject);
	            }
	            
	            if (substitutions!=null)
	            {
	                message.setText(String.format(message.getText(), substitutions));
	            }
	           
	            mailSender.send(message);
	                       
	      }
	      catch (Exception e)
	      {
	          logger.error("Issue with sending email",e);
	      }
	            
	    }

	  }
	  
	  public void sendEmailUsingTemplate(String from, List<String> to, String subject, String[] substitutions, String template) throws MessagingException
	  {
	      logger.debug("sendEmailUsingTemplate called");
	      taskExecutor.execute(new MailTask(from,to,subject,substitutions,template));
	  }

}
