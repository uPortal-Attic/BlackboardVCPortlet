/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.io.Serializable;

import org.jasig.portlet.blackboardvcportlet.data.RecordingMode;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.data.Session;


/**
 * form backing object for creating/editing sessions
 */
public class FullSessionForm extends LimitedSessionForm implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int maxTalkers;
	private int maxCameras;
	private boolean mustBeSupervised;
	private boolean permissionsOn;
	private boolean raiseHandOnEnter;
	private RecordingMode recordingMode;
	private boolean hideParticipantNames;
	private boolean allowInSessionInvites;
	
	public FullSessionForm(ServerConfiguration serverConfiguration) {
	    super(serverConfiguration);
	    
	    this.maxCameras = serverConfiguration.getMaxAvailableCameras();
	    this.maxTalkers = serverConfiguration.getMaxAvailableTalkers();
	    this.raiseHandOnEnter = serverConfiguration.isRaiseHandOnEnter(); 
	}
	
	public FullSessionForm(Session session) {
	    super(session);

	    this.maxTalkers = session.getMaxTalkers();
	    this.maxCameras = session.getMaxCameras();
	    this.mustBeSupervised = session.isMustBeSupervised();
	    this.permissionsOn = session.isPermissionsOn();
	    this.raiseHandOnEnter = session.isRaiseHandOnEnter();
	    this.recordingMode = session.getRecordingMode();
	    this.hideParticipantNames = session.isHideParticipantNames();
	    this.allowInSessionInvites = session.isAllowInSessionInvites();
	}

    public int getMaxTalkers() {
        return maxTalkers;
    }

    public void setMaxTalkers(int maxTalkers) {
        this.maxTalkers = maxTalkers;
    }

    public int getMaxCameras() {
        return maxCameras;
    }

    public void setMaxCameras(int maxCameras) {
        this.maxCameras = maxCameras;
    }

    public boolean isMustBeSupervised() {
        return mustBeSupervised;
    }

    public void setMustBeSupervised(boolean mustBeSupervised) {
        this.mustBeSupervised = mustBeSupervised;
    }

    public boolean isPermissionsOn() {
        return permissionsOn;
    }

    public void setPermissionsOn(boolean permissionsOn) {
        this.permissionsOn = permissionsOn;
    }

    public boolean isRaiseHandOnEnter() {
        return raiseHandOnEnter;
    }

    public void setRaiseHandOnEnter(boolean raiseHandOnEnter) {
        this.raiseHandOnEnter = raiseHandOnEnter;
    }

    public RecordingMode getRecordingMode() {
        return recordingMode;
    }

    public void setRecordingMode(RecordingMode recordingMode) {
        this.recordingMode = recordingMode;
    }

    public boolean isHideParticipantNames() {
        return hideParticipantNames;
    }

    public void setHideParticipantNames(boolean hideParticipantNames) {
        this.hideParticipantNames = hideParticipantNames;
    }

    public boolean isAllowInSessionInvites() {
        return allowInSessionInvites;
    }

    public void setAllowInSessionInvites(boolean allowInSessionInvites) {
        this.allowInSessionInvites = allowInSessionInvites;
    }

    @Override
    public String toString() {
        return "FullSessionForm [maxTalkers=" + maxTalkers + ", maxCameras=" + maxCameras + ", mustBeSupervised="
                + mustBeSupervised + ", permissionsOn=" + permissionsOn + ", raiseHandOnEnter=" + raiseHandOnEnter
                + ", recordingMode=" + recordingMode + ", hideParticipantNames=" + hideParticipantNames
                + ", allowInSessionInvites=" + allowInSessionInvites + ", isNewSession()=" + isNewSession()
                + ", getSessionId()=" + getSessionId() + ", getSessionName()=" + getSessionName() + ", getStartTime()="
                + getStartTime() + ", getEndTime()=" + getEndTime() + ", getBoundaryTime()=" + getBoundaryTime() + "]";
    }
}