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
package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 * Entity class for storing Sessions
 * @author Richard Good
 */
@Entity
@Table(name="VC_SESSION")
public class Session implements Serializable {
    
    @Id
    @Column(name="SESSION_ID")
    protected long sessionId;
    
    @Column(name="SESSION_NAME")
    protected String sessionName;
    
    @Column(name="START_TIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date startTime;
    
    @Column(name="END_TIME")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date endTime;
    
    @Column(name="CREATOR_ID")
    protected String creatorId;
    
    @Column(name="BOUNDARY_TIME")
    protected int boundaryTime;
    
    @Column(name="ACCESS_TYPE")
    protected long accessType;
    
    @Column(name="RECORDINGS")
    protected boolean recordings;
    
    @Column(name="CHAIR_NOTES")
    protected String chairNotes;
    
    @Column(name="NON_CHAIR_NOTES")
    protected String nonChairNotes;
    
    @Column(name="CHAIR_LIST")
    protected String chairList;
    
    @Column(name="NON_CHAIR_LIST")
    protected String nonChairList;
    
    @Column(name="GROUPING_LIST")
    protected String groupingList;
    
    @Column(name="OPEN_CHAIR")
    protected boolean openChair;
    
    @Column(name="PERMISSIONS_ON")
    protected boolean permissionsOn;
    
    @Column(name="MUST_BE_SUPERVISED")
    protected boolean mustBeSupervised;
    
    @Column(name="RECORDING_MODE_TYPE")
    protected long recordingModeType;
    
    @Column(name="MAX_TALKERS")
    protected int maxTalkers;
    
    @Column(name="MAX_CAMERAS")
    protected int maxCameras;
    
    @Column(name="RAISE_HAND_ON_ENTER")
    protected boolean raiseHandOnEnter;
    
    @Column(name="RESERVE_SEATS")
    protected int reserveSeats;
    
    @Column(name="SECURE_SIGN_ON")
    protected boolean secureSignOn;
    
    @Column(name="VERSION_ID")
    protected long versionId;
    
    @Column(name="ALLOW_IN_SESSION_INVITES")
    protected boolean allowInSessionInvites;
    
    @Column(name="HIDE_PARTICIPANT_NAMES")
    protected boolean hideParticipantNames;
    
    @Column(name="LAST_UPDATED")
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date lastUpdated;
    
    @Column(name="CREATOR_ORGUNIT")
    protected String creatorOrgUnit;
    
    @Transient
    protected boolean currUserCanEdit;

    public boolean isCurrUserCanEdit() {
        return currUserCanEdit;
    }

    public void setCurrUserCanEdit(boolean currUserCanEdit) {
        this.currUserCanEdit = currUserCanEdit;
    }
    
    public String getCreatorOrgUnit() {
        return creatorOrgUnit;
    }

    public void setCreatorOrgUnit(String creatorOrgUnit) {
        this.creatorOrgUnit = creatorOrgUnit;
    }

    public long getAccessType() {
        return accessType;
    }

    public void setAccessType(long accessType) {
        this.accessType = accessType;
    }

    public boolean isAllowInSessionInvites() {
        return allowInSessionInvites;
    }

    public void setAllowInSessionInvites(boolean allowInSessionInvites) {
        this.allowInSessionInvites = allowInSessionInvites;
    }

    public int getBoundaryTime() {
        return boundaryTime;
    }

    public void setBoundaryTime(int boundaryTime) {
        this.boundaryTime = boundaryTime;
    }

    public String getChairList() {
        return chairList;
    }

    public void setChairList(String chairList) {
        this.chairList = chairList;
    }

    public String getChairNotes() {
        return chairNotes;
    }

    public void setChairNotes(String chairNotes) {
        this.chairNotes = chairNotes;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getGroupingList() {
        return groupingList;
    }

    public void setGroupingList(String groupingList) {
        this.groupingList = groupingList;
    }

    public boolean isHideParticipantNames() {
        return hideParticipantNames;
    }

    public void setHideParticipantNames(boolean hideParticipantNames) {
        this.hideParticipantNames = hideParticipantNames;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getMaxCameras() {
        return maxCameras;
    }

    public void setMaxCameras(int maxCameras) {
        this.maxCameras = maxCameras;
    }

    public int getMaxTalkers() {
        return maxTalkers;
    }

    public void setMaxTalkers(int maxTalkers) {
        this.maxTalkers = maxTalkers;
    }

    public boolean isMustBeSupervised() {
        return mustBeSupervised;
    }

    public void setMustBeSupervised(boolean mustBeSupervised) {
        this.mustBeSupervised = mustBeSupervised;
    }

    public String getNonChairList() {
        return nonChairList;
    }

    public void setNonChairList(String nonChairList) {
        this.nonChairList = nonChairList;
    }

    public String getNonChairNotes() {
        return nonChairNotes;
    }

    public void setNonChairNotes(String nonChairNotes) {
        this.nonChairNotes = nonChairNotes;
    }

    public boolean isOpenChair() {
        return openChair;
    }

    public void setOpenChair(boolean openChair) {
        this.openChair = openChair;
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

    public long getRecordingModeType() {
        return recordingModeType;
    }

    public void setRecordingModeType(long recordingModeType) {
        this.recordingModeType = recordingModeType;
    }

    public boolean isRecordings() {
        return recordings;
    }

    public void setRecordings(boolean recordings) {
        this.recordings = recordings;
    }

    public int getReserveSeats() {
        return reserveSeats;
    }

    public void setReserveSeats(int reserveSeats) {
        this.reserveSeats = reserveSeats;
    }

    public boolean isSecureSignOn() {
        return secureSignOn;
    }

    public void setSecureSignOn(boolean secureSignOn) {
        this.secureSignOn = secureSignOn;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }
      
}
