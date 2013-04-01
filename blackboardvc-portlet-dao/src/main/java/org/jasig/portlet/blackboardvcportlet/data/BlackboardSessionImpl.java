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

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.joda.time.DateTime;

import com.elluminate.sas.SessionResponse;

@Entity
@Table(name = "VC2_SESSION")
@SequenceGenerator(
        name="VC2_SESSION_GEN",
        sequenceName="VC2_SESSION_SEQ",
        allocationSize=10
    )
@TableGenerator(
        name="VC2_SESSION_GEN",
        pkColumnValue="VC2_SESSION",
        allocationSize=10
    )
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BlackboardSessionImpl implements BlackboardSession {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "VC2_SESSION_GEN")
    @Column(name = "SESSION_ID")
    private final long sessionId;
    
    @Version
    @Column(name = "ENTITY_VERSION")
    private final long entityVersion;
    
    @NaturalId
    @Column(name="BB_SESSION_ID")
    private final long bbSessionId;
    
    @Column(name="SESSION_NAME", length = 1000)
    private final String sessionName;
    
    @Column(name="START_TIME")
    private final DateTime startTime;
    
    @Column(name="END_TIME")
    private final DateTime endTime;
    
    @ManyToOne(targetEntity = BlackboardUserImpl.class, optional = false)
    @JoinColumn(name = "CREATOR", nullable = false)
    private BlackboardUser creator;
    
    private Integer boundaryTime;
    private Long accessType;
    private Boolean recordings;
    private String chairNotes;
    private String nonChairNotes;
    
    @ManyToMany(targetEntity = BlackboardUserImpl.class, fetch = FetchType.LAZY)
    @JoinTable(name="VC2_SESSION_CHAIRS",
        joinColumns= @JoinColumn(name="SESSION_ID"),
        inverseJoinColumns=@JoinColumn(name="USER_ID")
    )
    private Set<BlackboardUser> chairs;
    
    @ManyToMany(targetEntity = BlackboardUserImpl.class, fetch = FetchType.LAZY)
    @JoinTable(name="VC2_SESSION_NONCHAIRS",
        joinColumns= @JoinColumn(name="SESSION_ID"),
        inverseJoinColumns=@JoinColumn(name="USER_ID")
    )
    private Set<BlackboardUser> nonChairs;
    
    private Boolean openChair;
    private Boolean mustBeSupervised;
    private Long recordingMode;
    private Integer maxTalkers;
    private Integer maxCameras;
    private Boolean raiseHandOnEnter;
    private Integer reserveSeats;
    private Long bbVersion;
    private Boolean allowInSessionInvites;
    private Boolean hideParticipantNames;
    
    public BlackboardSessionImpl(SessionResponse sessionResponse) {
        this.sessionId = -1;
        this.entityVersion = -1;
        
        this.bbSessionId = sessionResponse.getSessionId();
        this.sessionName = sessionResponse.getSessionName();
        this.startTime = new DateTime(sessionResponse.getStartTime());
        this.endTime = new DateTime(sessionResponse.getEndTime());
        this.creator = null; //TODO how to look this up from sessionResponse.getCreatorId()
        this.boundaryTime = sessionResponse.getBoundaryTime();
        this.accessType = sessionResponse.getAccessType();
        this.recordings = sessionResponse.isRecordings();
        this.chairNotes = sessionResponse.getChairNotes();
        this.nonChairNotes = sessionResponse.getNonChairNotes();
        this.chairs = null;//TODO
        this.nonChairs = null;//TODO
        this.openChair = sessionResponse.isOpenChair();
        this.mustBeSupervised = sessionResponse.isMustBeSupervised();
        this.recordingMode = sessionResponse.getRecordingModeType();
        this.maxTalkers = sessionResponse.getMaxTalkers();
        this.maxCameras = sessionResponse.getMaxCameras();
        this.raiseHandOnEnter = sessionResponse.isRaiseHandOnEnter();
        this.reserveSeats = sessionResponse.getReserveSeats();
        this.bbVersion = sessionResponse.getVersionId();
        this.allowInSessionInvites = sessionResponse.isAllowInSessionInvites();
        this.hideParticipantNames = sessionResponse.isHideParticipantNames();
    }
    
}
