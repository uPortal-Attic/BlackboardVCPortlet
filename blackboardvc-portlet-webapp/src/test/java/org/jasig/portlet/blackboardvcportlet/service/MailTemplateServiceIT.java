/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasig.portlet.blackboardvcportlet.data.AccessType;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.service.impl.MailTemplateServiceImpl;
import org.jasig.portlet.blackboardvcportlet.service.util.MailMessages;
import org.jasig.springframework.mockito.MockitoFactoryBean;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-emailContext.xml")
public class MailTemplateServiceIT
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MailTemplateServiceImpl mailTemplateService;
	
	@Mock ConferenceUser user;
	@Mock Session session;
	@Mock SessionService sessionService;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		MockitoFactoryBean.resetAllMocks();
		mailTemplateService.setSessionService(sessionService);
	}
	
	@Test
	public void testBuildEmailMessage() throws Exception
	{
		logger.info("testBuildEmailMessage() starts...");
		assertNotNull(mailTemplateService);

		String from = "from@wisc.edu";
		List<String> to = new ArrayList<String>();
		to.add("to@wisc.edu");

		Map<String, String> subs = new HashMap<String, String>();
		subs.put("displayName", "Display Name");
		subs.put("creatorDetails", "Creator Details");
		subs.put("sessionName", "Session Name");
		subs.put("sessionStartTime", "12 PM on 4/1/2013");
		subs.put("sessionEndTime", "3 PM on 4/1/2013");
		subs.put("userURL", "http://www.wisc.edu");

		MailTask mailTask = new MailTask(from, to, null, subs, MailMessages.MODERATOR);
		assertNotNull(mailTask);

		String message = mailTemplateService.buildEmailMessage(mailTask);
		assertNotNull(message);
		logger.info("Email Message: {}", message);
		logger.info("testBuildEmailMessage() ends.");
	}
	
	@Test
	public void testSendingMail() throws Exception {
		setupWhenClausesForEmail();
		mailTemplateService.setFrom("dalquist@wisc.edu");
		mailTemplateService.sendMail(mailTemplateService.buildCancellationNoticeMailTask(user, session));
		
	}
	
	@Test
	public void testSendingEmailWithAttachment() throws Exception {
		setupWhenClausesForEmail();
		mailTemplateService.setFrom("dalquist@wisc.edu");
		mailTemplateService.sendMail(mailTemplateService.buildModeratorMailTask(user, session, false));
	}
	
	@Test
	public void testSendingInviteUpdateAndCancellation() throws Exception {
		setupWhenClausesForEmail();
		mailTemplateService.setFrom("dalquist@wisc.edu");
		//send initial email invite
		mailTemplateService.sendMail(mailTemplateService.buildModeratorMailTask(user, session, false));
		
		Thread.sleep(10000); //wait 10 seconds
		//send update email with later end time (yah long meetings)
		when(session.getEndTime()).thenReturn(new DateTime().plusHours(3));
		mailTemplateService.sendMail(mailTemplateService.buildModeratorMailTask(user, session, true));
		
		Thread.sleep(10000);//wait 10 seconds
		//send cancellation notice
		mailTemplateService.sendMail(mailTemplateService.buildCancellationNoticeMailTask(user, session));
		
	}
	
	private void setupWhenClausesForEmail() {
		when(user.getDisplayName()).thenReturn("Tim Levett");
		when(user.getEmail()).thenReturn("levett@wisc.edu");
		when(session.getAccessType()).thenReturn(AccessType.PRIVATE);
		when(session.getSessionName()).thenReturn("Session Name");
		when(session.getEndTime()).thenReturn(new DateTime().plusHours(2));
		when(session.getStartTime()).thenReturn(new DateTime().plusHours(1));
		when(session.getCreator()).thenReturn(user);
		when(session.getGuestUrl()).thenReturn("http://www.example.com/invite/guestURL");
		when(session.getBoundaryTime()).thenReturn(30);
		when(sessionService.getOrCreateSessionUrl(any(ConferenceUser.class), any(Session.class))).thenReturn("http://www.example.com/inviteurlforuser");
	}
}
