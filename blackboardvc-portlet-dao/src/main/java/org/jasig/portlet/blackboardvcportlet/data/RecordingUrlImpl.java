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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Entity class for storing recording URLs
 * @author Richard Good
 */
@Entity
@Table(name="VC_RECORDING_URL")
public class RecordingUrlImpl implements RecordingUrl {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected RecordingUrlId recordingUrlId;
    
    @Column(name="LAST_UPDATED")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date lastUpdated;

    public RecordingUrlImpl()
    {
        recordingUrlId = new RecordingUrlId();
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
    public void setRecordingId(Long recordingId)
    {
        this.recordingUrlId.recordingId=recordingId;
    }
    
    @Override
    public Long getRecordingId()
    {
        return this.recordingUrlId.recordingId;
    }
    
    @Override
    public String getUrl()
    {
        return this.recordingUrlId.url;
    }
    
    @Override
    public void setUrl(String url)
    {
        this.recordingUrlId.url=url;
    }
}
