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
package org.jasig.portlet.blackboardvcportlet.dao.impl;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;
import org.joda.time.DateTime;

/**
 * Entity class for server quota
 * @author Richard Good
 */
@Entity
@Table(name = "VC2_SERVER_QUOTA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServerQuotaImpl implements ServerQuota {
    public static final String QUOTA_ID = "bsq";
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "QUOTA_ID", nullable = false)
    private final String quotaId = QUOTA_ID;
    
    @Version
    @Column(name = "ENTITY_VERSION", nullable = false)
    private final long entityVersion;

    @Column(name="DISK_QUOTA", nullable = false)
    private long diskQuota;
    
    @Column(name="DISK_QUOTA_AVAILABLE", nullable = false)
    private long diskQuotaAvailable;
    
    @Column(name="SESSION_QUOTA", nullable = false)
    private int sessionQuota;
    
    @Column(name="SESSION_QUOTA_AVAILABLE", nullable = false)
    private int sessionQuotaAvailable;
    
    @Column(name="LAST_UPDATED", nullable = false)
    @Type(type = "dateTime")
    private DateTime lastUpdated;    
    
    
    ServerQuotaImpl() {
        this.entityVersion = -1;
    }

    /**
     * Used to keep lastUpdated up to date
     */
    protected final void onUpdate() {
        lastUpdated = DateTime.now();
    }

    @Override
    public long getDiskQuota() {
        return diskQuota;
    }

    public void setDiskQuota(long diskQuota) {
        this.diskQuota = diskQuota;
    }

    @Override
    public long getDiskQuotaAvailable() {
        return diskQuotaAvailable;
    }

    public void setDiskQuotaAvailable(long diskQuotaAvailable) {
        this.diskQuotaAvailable = diskQuotaAvailable;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public int getSessionQuota() {
        return sessionQuota;
    }

    public void setSessionQuota(int sessionQuota) {
        this.sessionQuota = sessionQuota;
    }

    @Override
    public int getSessionQuotaAvailable() {
        return sessionQuotaAvailable;
    }

    public void setSessionQuotaAvailable(int sessionQuotaAvailable) {
        this.sessionQuotaAvailable = sessionQuotaAvailable;
    }
    
    //********** NOTE ALL ServerQuotaImpl instances are equal! **********//

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quotaId == null) ? 0 : quotaId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerQuotaImpl other = (ServerQuotaImpl) obj;
        if (quotaId == null) {
            if (other.quotaId != null)
                return false;
        }
        else if (!quotaId.equals(other.quotaId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServerQuotaImpl [quotaId=" + quotaId + ", entityVersion=" + entityVersion + ", diskQuota=" + diskQuota
                + ", diskQuotaAvailable=" + diskQuotaAvailable + ", sessionQuota=" + sessionQuota
                + ", sessionQuotaAvailable=" + sessionQuotaAvailable + ", lastUpdated=" + lastUpdated + "]";
    }
}
