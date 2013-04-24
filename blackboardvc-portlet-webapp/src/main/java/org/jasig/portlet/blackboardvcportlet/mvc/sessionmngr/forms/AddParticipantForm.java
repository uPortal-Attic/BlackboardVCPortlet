/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 3:02 PM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AddParticipantForm implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NotNull
	private long sessionId;
	private String participantName;

	@Length(min = 1)
	@Email
	private String emailAddress;

	/**
	 * Constructor
	 */
	public AddParticipantForm()
	{
	}

	public String getParticipantName()
	{
		return participantName;
	}

	public void setParticipantName(String participantName)
	{
		this.participantName = participantName;
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
		StringBuilder sb = new StringBuilder("AddParticipantForm [participantName=");
		sb.append(participantName);
		sb.append(", emailAddress=");
		sb.append(emailAddress);
		sb.append("];");
		return sb.toString();
	}
}
