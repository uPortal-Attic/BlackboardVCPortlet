/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.service;

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
import javax.mail.MessagingException;
import java.util.List;

/**
 * Class which allows the sending of email from a template
 * @author Richard Good
 */
@Service
public class MailTemplateService implements BeanFactoryAware
{
	private static Logger logger = LoggerFactory.getLogger(MailTemplateService.class);

    @Autowired
    private JavaMailSender mailSender;
    
    BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory bf) throws BeansException {
        beanFactory = bf;
    }
    
    // Runnable child class which allows the asynchronous sending of email
    private class MailTask implements Runnable {
        
    private String from;
    private List<String> to;
    private String subject;
    private String[] substitutions;
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
          logger.error("Exception caught",e);
      }
            
    }

  }
    
  private TaskExecutor taskExecutor;

  // Instantiate a task executor MailService
  public MailTemplateService(TaskExecutor taskExecutor) {
    this.taskExecutor = taskExecutor;
  }
        
  
  /**
   * Public method to execute an asynchronous email send
   * @param from
   * @param to
   * @param subject
   * @param substitutions
   * @param template
   * @throws MessagingException 
   */
  public void sendEmailUsingTemplate(String from, List<String> to, String subject, String[] substitutions, String template) throws MessagingException
  {
      logger.debug("sendEmailUsingTemplate called");
      taskExecutor.execute(new MailTask(from,to,subject,substitutions,template));
  }
  
}
