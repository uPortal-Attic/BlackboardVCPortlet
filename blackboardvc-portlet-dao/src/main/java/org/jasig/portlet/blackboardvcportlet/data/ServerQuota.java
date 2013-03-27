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
 * Entity class for server quota
 * @author Richard Good
 */
@Entity
@Table(name="VC_SERVER_QUOTA")
public class ServerQuota implements Serializable {
    
    @Id
    @Column(name="DISK_QUOTA")
    protected long diskQuota;
    
    @Column(name="DISK_QUOTA_AVAILABLE")
    protected long diskQuotaAvailable;
    
    @Column(name="SESSION_QUOTA")
    protected int sessionQuota;
    
    @Column(name="SESSION_QUOTA_AVAILABLE")
    protected int sessionQuotaAvailable;
    
    @Column(name="LAST_UPDATED")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date lastUpdated;

    public long getDiskQuota() {
        return diskQuota;
    }

    public void setDiskQuota(long diskQuota) {
        this.diskQuota = diskQuota;
    }

    public long getDiskQuotaAvailable() {
        return diskQuotaAvailable;
    }

    public void setDiskQuotaAvailable(long diskQuotaAvailable) {
        this.diskQuotaAvailable = diskQuotaAvailable;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getSessionQuota() {
        return sessionQuota;
    }

    public void setSessionQuota(int sessionQuota) {
        this.sessionQuota = sessionQuota;
    }

    public int getSessionQuotaAvailable() {
        return sessionQuotaAvailable;
    }

    public void setSessionQuotaAvailable(int sessionQuotaAvailable) {
        this.sessionQuotaAvailable = sessionQuotaAvailable;
    }
    
    
}
