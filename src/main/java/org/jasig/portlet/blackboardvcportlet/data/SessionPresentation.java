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

/**
 * Entity class for storing session presentations
 * @author Richard Good
 */
@Entity
@Table(name="VC_SESSION_PRESENTATION")
public class SessionPresentation implements Serializable{
    
    @Id
    @Column(name="PRESENTATION_ID")
    long presentationId;
    
    @Column(name="SESSION_ID")
    String sessionId;
    
    @Column(name="FILENAME")
    String fileName;
    
    @Column(name="DESCRIPTION")
    String description;
    
    @Column(name="CREATOR_ID")
    String creatorId;
    
    @Column(name="DATE_CREATED")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date dateUploaded;

    public long getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(long presentationId) {
        this.presentationId = presentationId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
    
}
