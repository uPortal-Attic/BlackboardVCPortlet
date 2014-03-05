/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 3:02 PM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class AddParticipantForm implements Serializable
{
	private static final long serialVersionUID = 1L;

	@NotNull
	private long sessionId;
	
	private String uniqueId;
	
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
    
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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
        return "AddParticipantForm [sessionId=" + sessionId + ", uniqueId=" + uniqueId + ", name=" + name + ", email="
                + email + ", moderator=" + moderator + "]";
    }
}
