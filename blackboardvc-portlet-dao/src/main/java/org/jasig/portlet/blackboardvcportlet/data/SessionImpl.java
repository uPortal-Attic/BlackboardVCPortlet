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
public class SessionImpl implements Session {
    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean isCurrUserCanEdit() {
        return currUserCanEdit;
    }

    @Override
    public void setCurrUserCanEdit(boolean currUserCanEdit) {
        this.currUserCanEdit = currUserCanEdit;
    }
    
    @Override
    public String getCreatorOrgUnit() {
        return creatorOrgUnit;
    }

    @Override
    public void setCreatorOrgUnit(String creatorOrgUnit) {
        this.creatorOrgUnit = creatorOrgUnit;
    }

    @Override
    public long getAccessType() {
        return accessType;
    }

    @Override
    public void setAccessType(long accessType) {
        this.accessType = accessType;
    }

    @Override
    public boolean isAllowInSessionInvites() {
        return allowInSessionInvites;
    }

    @Override
    public void setAllowInSessionInvites(boolean allowInSessionInvites) {
        this.allowInSessionInvites = allowInSessionInvites;
    }

    @Override
    public int getBoundaryTime() {
        return boundaryTime;
    }

    @Override
    public void setBoundaryTime(int boundaryTime) {
        this.boundaryTime = boundaryTime;
    }

    @Override
    public String getChairList() {
        return chairList;
    }

    @Override
    public void setChairList(String chairList) {
        this.chairList = chairList;
    }

    @Override
    public String getChairNotes() {
        return chairNotes;
    }

    @Override
    public void setChairNotes(String chairNotes) {
        this.chairNotes = chairNotes;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getGroupingList() {
        return groupingList;
    }

    @Override
    public void setGroupingList(String groupingList) {
        this.groupingList = groupingList;
    }

    @Override
    public boolean isHideParticipantNames() {
        return hideParticipantNames;
    }

    @Override
    public void setHideParticipantNames(boolean hideParticipantNames) {
        this.hideParticipantNames = hideParticipantNames;
    }

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int getMaxCameras() {
        return maxCameras;
    }

    @Override
    public void setMaxCameras(int maxCameras) {
        this.maxCameras = maxCameras;
    }

    @Override
    public int getMaxTalkers() {
        return maxTalkers;
    }

    @Override
    public void setMaxTalkers(int maxTalkers) {
        this.maxTalkers = maxTalkers;
    }

    @Override
    public boolean isMustBeSupervised() {
        return mustBeSupervised;
    }

    @Override
    public void setMustBeSupervised(boolean mustBeSupervised) {
        this.mustBeSupervised = mustBeSupervised;
    }

    @Override
    public String getNonChairList() {
        return nonChairList;
    }

    @Override
    public void setNonChairList(String nonChairList) {
        this.nonChairList = nonChairList;
    }

    @Override
    public String getNonChairNotes() {
        return nonChairNotes;
    }

    @Override
    public void setNonChairNotes(String nonChairNotes) {
        this.nonChairNotes = nonChairNotes;
    }

    @Override
    public boolean isOpenChair() {
        return openChair;
    }

    @Override
    public void setOpenChair(boolean openChair) {
        this.openChair = openChair;
    }

    @Override
    public boolean isPermissionsOn() {
        return permissionsOn;
    }

    @Override
    public void setPermissionsOn(boolean permissionsOn) {
        this.permissionsOn = permissionsOn;
    }

    @Override
    public boolean isRaiseHandOnEnter() {
        return raiseHandOnEnter;
    }

    @Override
    public void setRaiseHandOnEnter(boolean raiseHandOnEnter) {
        this.raiseHandOnEnter = raiseHandOnEnter;
    }

    @Override
    public long getRecordingModeType() {
        return recordingModeType;
    }

    @Override
    public void setRecordingModeType(long recordingModeType) {
        this.recordingModeType = recordingModeType;
    }

    @Override
    public boolean isRecordings() {
        return recordings;
    }

    @Override
    public void setRecordings(boolean recordings) {
        this.recordings = recordings;
    }

    @Override
    public int getReserveSeats() {
        return reserveSeats;
    }

    @Override
    public void setReserveSeats(int reserveSeats) {
        this.reserveSeats = reserveSeats;
    }

    @Override
    public boolean isSecureSignOn() {
        return secureSignOn;
    }

    @Override
    public void setSecureSignOn(boolean secureSignOn) {
        this.secureSignOn = secureSignOn;
    }

    @Override
    public long getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getSessionName() {
        return sessionName;
    }

    @Override
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getVersionId() {
        return versionId;
    }

    @Override
    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }
      
}
