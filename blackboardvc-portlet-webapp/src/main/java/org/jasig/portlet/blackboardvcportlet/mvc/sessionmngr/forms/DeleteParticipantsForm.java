/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/24/13 at 3:05 PM
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;

public class DeleteParticipantsForm implements Serializable
{
	private static final long serialVersionUID = 1L;

    @NotNull
    private long sessionId;

    @NotNull
    @Size(min = 1)
    private long[] id;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long[] getId() {
        return id;
    }

    public void setId(long[] id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeleteParticipantsForm [sessionId=" + sessionId + ", id=" + Arrays.toString(id) + "]";
    }
}

