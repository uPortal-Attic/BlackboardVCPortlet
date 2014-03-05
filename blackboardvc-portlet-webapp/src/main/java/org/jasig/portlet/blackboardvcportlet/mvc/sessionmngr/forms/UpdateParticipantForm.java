/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 3:05 PM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class UpdateParticipantForm implements Serializable
{
	private static final long serialVersionUID = 1L;

    @NotNull
    private long sessionId;

    @NotNull
    private long id;

    @NotNull
    private boolean moderator;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isModerator() {
        return moderator;
    }

    public void setModerator(boolean moderator) {
        this.moderator = moderator;
    }

    @Override
    public String toString() {
        return "UpdateParticipantForm [sessionId=" + sessionId + ", id=" + id + ", moderator=" + moderator + "]";
    }
}

