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

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

/**
 * Class which allows the sending of email from a template
 * @author Richard Good
 */
@Service
public interface MailTemplateService {

    public void setBeanFactory(BeanFactory bf) throws BeansException;
        
  /**
   * Public method to execute an asynchronous email send
   * @param from
   * @param to
   * @param subject
   * @param substitutions
   * @param template
   * @throws MessagingException 
   */
  public void sendEmailUsingTemplate(String from, List<String> to, String subject, String[] substitutions, String template) throws MessagingException;
  
}
