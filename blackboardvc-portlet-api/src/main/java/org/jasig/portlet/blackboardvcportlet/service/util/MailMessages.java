/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/1/13 at 11:48 AM
 */
package org.jasig.portlet.blackboardvcportlet.service.util;

public enum MailMessages
{
	MODERATOR("moderatorMailMessage"),
	SESSION_DELETION("sessionDeletionMessage"),
	INTERNAL_PARTICIPANT("intParticipantMailMessage"),
	EXTERNAL_PARTICIPANT("extParticipantMailMessage");

	private String templateName;

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
}
