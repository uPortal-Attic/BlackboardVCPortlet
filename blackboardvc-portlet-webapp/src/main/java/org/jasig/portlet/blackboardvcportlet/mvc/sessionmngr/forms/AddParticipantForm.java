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
	
    @Length(min = 1)
    private String name;

	@Length(min = 1)
	@Email
	private String email;
	
	@NotNull
    private boolean moderator;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isModerator() {
        return moderator;
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }

    @Override
    public String toString() {
        return "AddParticipantForm [sessionId=" + sessionId + ", name=" + name + ", email=" + email + ", moderator="
                + moderator + "]";
    }
}
