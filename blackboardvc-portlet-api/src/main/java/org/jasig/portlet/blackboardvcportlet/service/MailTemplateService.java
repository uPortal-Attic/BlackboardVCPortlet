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

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser.Roles;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

/**
 * Class which allows the sending of email from a template
 *
 * @author Richard Good
 */
@Service
public interface MailTemplateService
{
	public void setBeanFactory(BeanFactory bf) throws BeansException;
	
	public void sendEmail(MailTask mailTask);
	
	public void buildAndSendSessionEmails(Session session, boolean isUpdate, boolean isFirstTime);
	public void buildAndSendCancelationMeetingEmail(Session session);
	
	public MailTask buildCancellationNoticeMailTask(ConferenceUser user, Session session);
	public MailTask buildParticipantMailTask(ConferenceUser participant, Session session, boolean isUpdate);
	public MailTask buildModeratorMailTask(ConferenceUser moderator, Session session, boolean isUpdate);

	public MailTask buildSwitchRolesEmail(ConferenceUser user, Session session, Roles newRole);
	
}
