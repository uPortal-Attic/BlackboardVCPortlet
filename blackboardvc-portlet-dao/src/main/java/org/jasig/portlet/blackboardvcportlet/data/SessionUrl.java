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
import javax.persistence.*;

/**
 * Entity class for storing session URLs
 * @author Richard Good
 */
@Entity
@Table(name="VC_SESSION_URL")
public class SessionUrl implements Serializable {
    
    @EmbeddedId
    protected SessionUrlId sessionUrlId;
       
    @Column(name="URL")
    protected String url;
    
    @Column(name="LAST_UPDATED")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date lastUpdated;

    public SessionUrl()
    {
        sessionUrlId = new SessionUrlId();
    }
    
    public String getDisplayName() {
        return this.sessionUrlId.displayName;
    }

    public void setDisplayName(String displayName) {
        this.sessionUrlId.displayName = displayName;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getSessionId() {
        return this.sessionUrlId.sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionUrlId.sessionId = sessionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return this.sessionUrlId.userId;
    }

    public void setUserId(String userId) {
        this.sessionUrlId.userId = userId;
    }
       
}
