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

/**
 * Entity class for server quota
 * @author Richard Good
 */
@Entity
@Table(name="VC_SERVER_QUOTA")
public class ServerQuotaImpl implements ServerQuota {
    private static final long serialVersionUID = 1L;

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

    @Override
    public long getDiskQuota() {
        return diskQuota;
    }

    @Override
    public void setDiskQuota(long diskQuota) {
        this.diskQuota = diskQuota;
    }

    @Override
    public long getDiskQuotaAvailable() {
        return diskQuotaAvailable;
    }

    @Override
    public void setDiskQuotaAvailable(long diskQuotaAvailable) {
        this.diskQuotaAvailable = diskQuotaAvailable;
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
    public int getSessionQuota() {
        return sessionQuota;
    }

    @Override
    public void setSessionQuota(int sessionQuota) {
        this.sessionQuota = sessionQuota;
    }

    @Override
    public int getSessionQuotaAvailable() {
        return sessionQuotaAvailable;
    }

    @Override
    public void setSessionQuotaAvailable(int sessionQuotaAvailable) {
        this.sessionQuotaAvailable = sessionQuotaAvailable;
    }
    
    
}
