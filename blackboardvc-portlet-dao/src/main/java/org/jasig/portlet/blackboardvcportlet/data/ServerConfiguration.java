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
 * Entity class for server configuration details
 * @author Richard Good
 */
@Entity
@Table(name="VC_SERVER_CONFIGURATION")
public class ServerConfiguration implements Serializable{
    
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getBoundaryTime() {
        return boundaryTime;
    }

    public void setBoundaryTime(int boundaryTime) {
        this.boundaryTime = boundaryTime;
    }

    public int getMaxAvailableCameras() {
        return maxAvailableCameras;
    }

    public void setMaxAvailableCameras(int maxAvailableCameras) {
        this.maxAvailableCameras = maxAvailableCameras;
    }

    public int getMaxAvailableTalkers() {
        return maxAvailableTalkers;
    }

    public void setMaxAvailableTalkers(int maxAvailableTalkers) {
        this.maxAvailableTalkers = maxAvailableTalkers;
    }

    public char getMayUseSecureSignOn() {
        return mayUseSecureSignOn;
    }

    public void setMayUseSecureSignOn(char mayUseSecureSignOn) {
        this.mayUseSecureSignOn = mayUseSecureSignOn;
    }

    public char getMustReserveSeats() {
        return mustReserveSeats;
    }

    public void setMustReserveSeats(char mustReserveSeats) {
        this.mustReserveSeats = mustReserveSeats;
    }

    public char getRaiseHandOnEnter() {
        return raiseHandOnEnter;
    }

    public void setRaiseHandOnEnter(char raiseHandOnEnter) {
        this.raiseHandOnEnter = raiseHandOnEnter;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    
    
}
