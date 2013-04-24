/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 11:04 AM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AddModeratorForm implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NotNull
	private long sessionId;
	private String moderatorName;

	@Length(min = 1)
	@Email
	private String emailAddress;

	/**
	 * Constructor
	 */
	public AddModeratorForm()
	{
	}

	public String getModeratorName()
	{
		return moderatorName;
	}

	public void setModeratorName(String moderatorName)
	{
		this.moderatorName = moderatorName;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public long getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(long sessionId)
	{
		this.sessionId = sessionId;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("AddModeratorForm [moderatorName=");
		sb.append(moderatorName);
		sb.append(", emailAddress=");
		sb.append(emailAddress);
		sb.append("];");
		return sb.toString();
	}
}
