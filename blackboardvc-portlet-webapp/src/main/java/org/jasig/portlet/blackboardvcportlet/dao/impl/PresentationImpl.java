package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.joda.time.DateTime;

@Entity
@Table(name = "VC2_PRESENTATION")
@SequenceGenerator(
        name="VC2_PRESENTATION_GEN",
        sequenceName="VC2_PRESENTATION_SEQ",
        allocationSize=10
    )
@TableGenerator(
        name="VC2_PRESENTATION_GEN",
        pkColumnValue="VC2_PRESENTATION",
        allocationSize=10
    )
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PresentationImpl implements Presentation {
	@Id
    @GeneratedValue(generator = "VC2_PRESENTATION_GEN")
    @Column(name = "PRESENTATION_ID", nullable = false)
    private final long presentationId;
	
    @Version
    @Column(name = "ENTITY_VERSION")
    private final long entityVersion;
	
	@NaturalId
	@Column(name = "BB_PRESENTATION_ID", nullable = false)
	private final long bbPresentationId;
	
	@ManyToOne(targetEntity = ConferenceUserImpl.class, optional = false)
    @JoinColumn(name = "CREATOR", nullable = false)
    private final ConferenceUser creator;
	
	@Column(name="DESCRIPTION", nullable = false, length = 1000)
    private String description;
	
	@Column(name="FILENAME", nullable = false, length = 1000)
    private String filename;
	
	@Column(name="FILE_SIZE", nullable = false)
    private long size;
	
	@Column(name="LAST_UPDATED", nullable = false)
    @Type(type = "dateTime")
    private DateTime lastUpdated;
	
	@ManyToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "presentations")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<Session> sessions = new HashSet<Session>(0);
	
	@SuppressWarnings("unused")
	private PresentationImpl() {
        this.bbPresentationId = -1;
        this.entityVersion = -1;
        this.presentationId = -1;
        this.creator = null;
        this.description = null;
        this.size = -1;
    }
    
	PresentationImpl(long bbPresentationId, ConferenceUser creator) {
        Validate.notNull(creator, "creator cannot be null");
        this.presentationId = -1;
        this.entityVersion = -1;
        this.size = -1;
        this.bbPresentationId = bbPresentationId;
        this.creator = creator;
    }
	
	/**
     * Used to keep lastUpdated up to date
     */
    @PreUpdate
    @PrePersist
    protected final void onUpdate() {
        lastUpdated = DateTime.now();
    }
    
    @Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	@Override
	public String getFilename() {
		return filename;
	}

	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Override
	public ConferenceUser getCreator() {
		return creator;
	}

	Set<Session> getSessions() {
		return sessions;
	}
	
	@Override
	public String toString() {
		return "PresentationImpl [presentationId=" + presentationId
				+ ", bbPresentationId=" + bbPresentationId + ", creator=" + creator
				+ ", description=" + description + ", size=" + size
				+ ", lastUpdated=" + lastUpdated + ", sessions=" + sessions
				+ "]";
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (bbPresentationId ^ (bbPresentationId >>> 32));
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
        PresentationImpl other = (PresentationImpl) obj;
        if (bbPresentationId != other.bbPresentationId)
            return false;
        return true;
    }
}
