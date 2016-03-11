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
/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/1/13 at 11:48 AM
 */
package org.jasig.portlet.blackboardvcportlet.service.util;

public enum MailMessages
{
	MODERATOR("moderatorMailMessage"),
	SESSION_DELETION("sessionDeletionMailMessage"),
	INTERNAL_PARTICIPANT("intParticipantMailMessage"),
	EXTERNAL_PARTICIPANT("extParticipantMailMessage");

	private String templateName;

	/**
	 * Constructor
	 * @param templateName The name of the template minus path and minus file extension
	 */
	private MailMessages(String templateName)
	{
		this.templateName = templateName;
	}

	public String getTemplateName()
	{
		return templateName;
	}

	@Override
	public String toString()
	{
		return getTemplateName();
	}

	/**
	 * Get the classpath to the template
	 * @return String containing classpath reference to Velocity template
	 */
	public String getClassPathToTemplate()
	{
		return  "/mail/" + getTemplateName() + ".vm";
	}
}
