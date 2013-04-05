package org.jasig.portlet.blackboardvcportlet.dao.impl;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.joda.time.DateTime;

@Entity
@Table(name = "VC2_RECORDING")
@SequenceGenerator(
        name="VC2_RECORDING_GEN",
        sequenceName="VC2_RECORDING_SEQ",
        allocationSize=10
    )
@TableGenerator(
        name="VC2_RECORDING_GEN",
        pkColumnValue="VC2_RECORDING",
        allocationSize=10
    )
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SessionRecordingImpl implements SessionRecording {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "VC2_RECORDING_GEN")
    @Column(name = "RECORDING_ID")
    private final long recordingId;
    
    @Version
    @Column(name = "ENTITY_VERSION")
    private final long entityVersion;
    
    @NaturalId
    @Column(name="BB_RECORDING_ID", nullable = false)
    private final long bbRecordingId;
    
    @Column(name="ROOM_START", nullable = false)
    @Type(type="dateTime")
    private DateTime roomStart;
    
    @Column(name="ROOM_END", nullable = false)
    @Type(type="dateTime")
    private DateTime roomEnd;
    
    @Column(name="RECORDING_URL", nullable = false, length = 2000)
    private String recordingUrl;
    
    @Column(name="SECURE_SIGN_ON", nullable = false)
    private boolean secureSignOn;
    
    @Column(name="CREATION_DATE", nullable = false)
    @Type(type="dateTime")
    private DateTime creationDate;
    
    @Column(name="RECORDING_SIZE", nullable = false)
    private long recordingSize;
    
    @Column(name="ROOM_NAME", nullable = false, length = 1000)
    private String roomName;

    @ManyToOne(targetEntity = SessionImpl.class, optional = false)
    @JoinColumn(name = "SESSION_ID", nullable = false)
    private Session session;
    
    /**
     * Needed by hibernate
     */
    @SuppressWarnings("unused")
    private SessionRecordingImpl() {
        this.recordingId = -1;
        this.entityVersion = -1;
        this.bbRecordingId = -1;
    }
    
    SessionRecordingImpl(long bbRecordingId) {
        this.recordingId = -1;
        this.entityVersion = -1;
        this.bbRecordingId = bbRecordingId;
    }

    @Override
    public long getRecordingId() {
        return recordingId;
    }

    @Override
    public long getBbRecordingId() {
        return bbRecordingId;
    }

    @Override
    public DateTime getRoomStart() {
        return roomStart;
    }

    @Override
    public DateTime getRoomEnd() {
        return roomEnd;
    }

    @Override
    public String getRecordingUrl() {
        return recordingUrl;
    }

    @Override
    public boolean isSecureSignOn() {
        return secureSignOn;
    }

    @Override
    public DateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public long getRecordingSize() {
        return recordingSize;
    }

    @Override
    public String getRoomName() {
        return roomName;
    }

    @Override
    public Session getSession() {
        return session;
    }
    
    public void setRoomStart(DateTime roomStart) {
        this.roomStart = roomStart;
    }

    public void setRoomEnd(DateTime roomEnd) {
        this.roomEnd = roomEnd;
    }

    public void setRecordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
    }

    public void setSecureSignOn(boolean secureSignOn) {
        this.secureSignOn = secureSignOn;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setRecordingSize(long recordingSize) {
        this.recordingSize = recordingSize;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    void setSession(Session session) {
        this.session = session;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (bbRecordingId ^ (bbRecordingId >>> 32));
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
        SessionRecordingImpl other = (SessionRecordingImpl) obj;
        if (bbRecordingId != other.bbRecordingId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SessionRecordingImpl [recordingId=" + recordingId + ", entityVersion=" + entityVersion
                + ", bbRecordingId=" + bbRecordingId + ", roomStart=" + roomStart + ", roomEnd=" + roomEnd
                + ", recordingUrl=" + recordingUrl + ", secureSignOn=" + secureSignOn + ", creationDate="
                + creationDate + ", recordingSize=" + recordingSize + ", roomName=" + roomName + ", sessionId=" + session.getBbSessionId()
                + "]";
    }
}
