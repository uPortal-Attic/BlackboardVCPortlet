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

import java.util.List;
import java.util.Map;

import net.fortuna.ical4j.model.Calendar;

import org.jasig.portlet.blackboardvcportlet.service.util.MailMessages;

@SuppressWarnings("rawtypes")
public class MailTask
{
	private String from;
	private List<String> to;
	private String subject;
	private Map substitutions;
	private MailMessages template;
	private Calendar meetingInvite;

	/**
	 * Constructor
	 * @param from From Address
	 * @param to To Addresses
	 * @param subject Email Subject
	 * @param substitutions Data for the email template
	 * @param template Email Message To Send
	 */
	public MailTask(String from, List<String> to, String subject, Map substitutions, MailMessages template)
	{
		this.setFrom(from);
		this.setTo(to);
		this.setSubject(subject);
		this.setSubstitutions(substitutions);
		this.setTemplate(template);
	}
	
	public MailTask(List<String> to, Map substitutions, MailMessages template) {
		this.setTo(to);
		this.setSubstitutions(substitutions);
		this.setTemplate(template);
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public List<String> getTo()
	{
		return to;
	}

	public void setTo(List<String> to)
	{
		this.to = to;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public Map getSubstitutions()
	{
		return substitutions;
	}

	public void setSubstitutions(Map substitutions)
	{
		this.substitutions = substitutions;
	}

	public MailMessages getTemplate()
	{
		return template;
	}

	public void setTemplate(MailMessages template)
	{
		this.template = template;
	}

	public Calendar getMeetingInvite() {
		return meetingInvite;
	}

	public void setMeetingInvite(Calendar meetingInvite) {
		this.meetingInvite = meetingInvite;
	}
}