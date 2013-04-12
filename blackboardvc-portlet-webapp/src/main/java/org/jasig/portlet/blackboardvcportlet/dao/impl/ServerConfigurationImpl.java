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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Entity class for server configuration details
 * @author Richard Good
 */
@Entity
@Table(name = "VC2_SERVER_CONFIG")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServerConfigurationImpl implements ServerConfiguration {
    public static final String CONFIG_ID = "bsc";
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CONFIG_ID", nullable = false)
    private final String configId = CONFIG_ID;
    
    @Version
    @Column(name = "ENTITY_VERSION", nullable = false)
    private final long entityVersion;
    
    @Column(name="BOUNDARY_TIME", nullable = false)
    private int boundaryTime;
    
    @Column(name="MAX_AVAILABLE_TALKERS", nullable = false)
    private int maxAvailableTalkers;
    
    @Column(name="MAX_AVAILABLE_CAMERAS", nullable = false)
    private int maxAvailableCameras;
    
    @Column(name="RAISE_HAND_ON_ENTER", nullable = false)
    private boolean raiseHandOnEnter;

    @Column(name="MAY_USE_TELEPHONY", nullable = false)
    private boolean mayUseTelephony;
    
    @Column(name="MAY_USE_SECURE_SIGN_ON", nullable = false)
    private boolean mayUseSecureSignOn;
    
    @Column(name="MUST_RESERVE_SEATS", nullable = false)
    private boolean mustReserveSeats;
    
    @Column(name="TIMEZONE", nullable = false)
    @Type(type="dateTimeZone")
    private DateTimeZone timezone;
    
    @Column(name="LAST_UPDATED", nullable = false)
    @Type(type = "dateTime")
    private DateTime lastUpdated;    
    
    @Column(name="CALLBACK_URL", nullable = false, length = 80)
    private String randomCallbackUrl;
    
    ServerConfigurationImpl() {
        this.entityVersion = -1;
    }

    /**
     * Used to keep lastUpdated up to date
     */
    protected final void onUpdate() {
        lastUpdated = DateTime.now();
    }
    
    @Override
    public String getRandomCallbackUrl() {
    	return randomCallbackUrl;
    }
    
    public void setRandomCallbackUrl(String value) {
    	this.randomCallbackUrl = value;
    }

    @Override
    public int getBoundaryTime() {
        return boundaryTime;
    }

    public void setBoundaryTime(int boundaryTime) {
        this.boundaryTime = boundaryTime;
    }

    @Override
    public int getMaxAvailableTalkers() {
        return maxAvailableTalkers;
    }

    public void setMaxAvailableTalkers(int maxAvailableTalkers) {
        this.maxAvailableTalkers = maxAvailableTalkers;
    }

    @Override
    public int getMaxAvailableCameras() {
        return maxAvailableCameras;
    }

    public void setMaxAvailableCameras(int maxAvailableCameras) {
        this.maxAvailableCameras = maxAvailableCameras;
    }

    @Override
    public boolean isRaiseHandOnEnter() {
        return raiseHandOnEnter;
    }

    public void setRaiseHandOnEnter(boolean raiseHandOnEnter) {
        this.raiseHandOnEnter = raiseHandOnEnter;
    }

    @Override
    public boolean isMayUseTelephony() {
        return mayUseTelephony;
    }

    public void setMayUseTelephony(boolean mayUseTelephony) {
        this.mayUseTelephony = mayUseTelephony;
    }

    @Override
    public boolean isMayUseSecureSignOn() {
        return mayUseSecureSignOn;
    }

    public void setMayUseSecureSignOn(boolean mayUseSecureSignOn) {
        this.mayUseSecureSignOn = mayUseSecureSignOn;
    }

    @Override
    public boolean isMustReserveSeats() {
        return mustReserveSeats;
    }

    public void setMustReserveSeats(boolean mustReserveSeats) {
        this.mustReserveSeats = mustReserveSeats;
    }

    @Override
    public DateTimeZone getTimezone() {
        return timezone;
    }

    public void setTimezone(DateTimeZone timezone) {
        this.timezone = timezone;
    }

    @Override
    public long getEntityVersion() {
        return entityVersion;
    }

    @Override
    public DateTime getLastUpdated() {
        return lastUpdated;
    }
    
    
    
    //********** NOTE ALL ServerConfigurationImpl instances are equal! **********//

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((configId == null) ? 0 : configId.hashCode());
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
        ServerConfigurationImpl other = (ServerConfigurationImpl) obj;
        if (configId == null) {
            if (other.configId != null)
                return false;
        }
        else if (!configId.equals(other.configId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServerConfigurationImpl [configId=" + configId + ", entityVersion=" + entityVersion + ", boundaryTime="
                + boundaryTime + ", maxAvailableTalkers=" + maxAvailableTalkers + ", maxAvailableCameras="
                + maxAvailableCameras + ", raiseHandOnEnter=" + raiseHandOnEnter + ", mayUseTelephony="
                + mayUseTelephony + ", mayUseSecureSignOn=" + mayUseSecureSignOn + ", mustReserveSeats="
                + mustReserveSeats + ", timezone=" + timezone + ", lastUpdated=" + lastUpdated + "]";
    }
}
