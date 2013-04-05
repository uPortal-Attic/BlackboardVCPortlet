package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.io.Serializable;

import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.joda.time.DateTime;

/**
 * form backing object for creating/editing sessions
 */
public class LimitedSessionForm implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean newSession;
    private long sessionId;
    private String sessionName;
    private DateTime startTime;
    private DateTime endTime;
    private int boundaryTime;

    public LimitedSessionForm(ServerConfiguration serverConfiguration) {
        this.newSession = true;
        
        this.boundaryTime = serverConfiguration.getBoundaryTime();
    }
    
    public LimitedSessionForm(Session blackboardSession) {
        this.newSession = false;
        
        this.sessionId = blackboardSession.getSessionId();
        this.sessionName = blackboardSession.getSessionName();
        this.startTime = blackboardSession.getStartTime();
        this.endTime = blackboardSession.getEndTime();
        this.boundaryTime = blackboardSession.getBoundaryTime();
    }

    public final boolean isNewSession() {
        return newSession;
    }

    public final void setNewSession(boolean newSession) {
        this.newSession = newSession;
    }

    public final long getSessionId() {
        return sessionId;
    }

    public final void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public final String getSessionName() {
        return sessionName;
    }

    public final void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public final DateTime getStartTime() {
        return startTime;
    }

    public final void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public final DateTime getEndTime() {
        return endTime;
    }

    public final void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public final int getBoundaryTime() {
        return boundaryTime;
    }

    public final void setBoundaryTime(int boundaryTime) {
        this.boundaryTime = boundaryTime;
    }

    @Override
    public String toString() {
        return "LimitedSessionForm [newSession=" + newSession + ", sessionId=" + sessionId + ", sessionName="
                + sessionName + ", startTime=" + startTime + ", endTime=" + endTime + ", boundaryTime=" + boundaryTime
                + "]";
    }
}