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

import org.jasig.portlet.blackboardvcportlet.service.impl.MailTemplateServiceImpl;
import org.jasig.portlet.blackboardvcportlet.service.util.MailMessages;
import org.jasig.portlet.blackboardvcportlet.service.util.MailTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-emailContext.xml")
public class MailTemplateServiceIT
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MailTemplateServiceImpl mailTemplateService;
	
	@Mock
	private JavaMailSenderImpl mailSender;
	
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
}
