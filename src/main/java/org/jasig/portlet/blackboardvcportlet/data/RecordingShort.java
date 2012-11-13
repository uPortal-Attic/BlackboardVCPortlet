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
import javax.persistence.Transient;

/**
 * Entity class for storing recordings.
 * @author Richard Good
 */
@Entity
@Table(name="VC_RECORDING_SHORT")
public class RecordingShort implements Serializable{
    
    @Id
    @Column(name="RECORDING_ID")
    protected long recordingId;
    
    @Column(name="CREATION_DATE")
    protected long creationDate;
    
    @Column(name="RECORDING_SIZE")
    protected long recordingSize;
    
    @Column(name="ROOM_NAME")
    protected String roomName;
    
    @Column(name="SESSION_ID")
    protected long sessionId;
    
    @Column(name="CHAIR_LIST")
    protected String chairList;
    
    @Column(name="NON_CHAIR_LIST")
    protected String nonChairList;
    
    @Transient
    protected String recordingUrl;
    
    @Transient
    protected boolean currUserCanDelete;
    
    @Transient
    protected Date createdDate;
    
    @Transient 
    protected String readableFileSize;

    public String getReadableFileSize() {
        return readableFileSize;
    }

    public void setReadableFileSize(String readableFileSize) {
        this.readableFileSize = readableFileSize;
    }

    public String getChairList() {
        return chairList;
    }

    public void setChairList(String chairList) {
        this.chairList = chairList;
    }

    public String getNonChairList() {
        return nonChairList;
    }

    public void setNonChairList(String nonChairList) {
        this.nonChairList = nonChairList;
    }
  
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public boolean isCurrUserCanDelete() {
        return currUserCanDelete;
    }

    public void setCurrUserCanDelete(boolean currUserCanDelete) {
        this.currUserCanDelete = currUserCanDelete;
    }
    
    public String getRecordingUrl() {
        return recordingUrl;
    }

    public void setRecordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
    }
    
    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(long recordingId) {
        this.recordingId = recordingId;
    }

    public long getRecordingSize() {
        return recordingSize;
    }

    public void setRecordingSize(long recordingSize) {
        this.recordingSize = recordingSize;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
        
}
