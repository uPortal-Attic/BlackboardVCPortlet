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
