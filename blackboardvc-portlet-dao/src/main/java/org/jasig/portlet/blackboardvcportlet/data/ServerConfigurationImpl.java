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
 * Entity class for server configuration details
 * @author Richard Good
 */
@Entity
@Table(name="VC_SERVER_CONFIGURATION")
public class ServerConfigurationImpl implements ServerConfiguration {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="BOUNDARY_TIME")
    protected int boundaryTime;
    
    @Column(name="RAISE_HAND_ON_ENTER")
    protected char raiseHandOnEnter;
    
    @Column(name="MAX_AVAILABLE_TALKERS")
    protected int maxAvailableTalkers;
    
    @Column(name="MAX_AVAILABLE_CAMERAS")
    protected int maxAvailableCameras;
    
    @Column(name="TIMEZONE")
    protected String timezone;
    
    @Column(name="MAY_USE_SECURE_SIGN_ON")
    protected char mayUseSecureSignOn;
    
    @Column(name="MUST_RESERVE_SEATS")
    protected char mustReserveSeats;
    
    @Column(name="LAST_UPDATED")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date lastUpdated;

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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
    public int getMaxAvailableCameras() {
        return maxAvailableCameras;
    }

    @Override
    public void setMaxAvailableCameras(int maxAvailableCameras) {
        this.maxAvailableCameras = maxAvailableCameras;
    }

    @Override
    public int getMaxAvailableTalkers() {
        return maxAvailableTalkers;
    }

    @Override
    public void setMaxAvailableTalkers(int maxAvailableTalkers) {
        this.maxAvailableTalkers = maxAvailableTalkers;
    }

    @Override
    public char getMayUseSecureSignOn() {
        return mayUseSecureSignOn;
    }

    @Override
    public void setMayUseSecureSignOn(char mayUseSecureSignOn) {
        this.mayUseSecureSignOn = mayUseSecureSignOn;
    }

    @Override
    public char getMustReserveSeats() {
        return mustReserveSeats;
    }

    @Override
    public void setMustReserveSeats(char mustReserveSeats) {
        this.mustReserveSeats = mustReserveSeats;
    }

    @Override
    public char getRaiseHandOnEnter() {
        return raiseHandOnEnter;
    }

    @Override
    public void setRaiseHandOnEnter(char raiseHandOnEnter) {
        this.raiseHandOnEnter = raiseHandOnEnter;
    }

    @Override
    public String getTimezone() {
        return timezone;
    }

    @Override
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    
    
}
