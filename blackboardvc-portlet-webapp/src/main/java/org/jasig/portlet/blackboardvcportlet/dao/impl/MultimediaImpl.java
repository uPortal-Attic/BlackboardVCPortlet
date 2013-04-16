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
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.joda.time.DateTime;

@Entity
@Table(name = "VC2_MULTIMEDIA")
@SequenceGenerator(
        name="VC2_MULTIMEDIA_GEN",
        sequenceName="VC2_MULTIMEDIA_SEQ",
        allocationSize=10
    )
@TableGenerator(
        name="VC2_MULTIMEDIA_GEN",
        pkColumnValue="VC2_MULTIMEDIA",
        allocationSize=10
    )
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MultimediaImpl implements Multimedia {

	@Id
    @GeneratedValue(generator = "VC2_MULTIMEDIA_GEN")
    @Column(name = "MULTIMEDIA_ID", nullable = false)
    private final long multimediaId;
	
    @Version
    @Column(name = "ENTITY_VERSION")
    private final long entityVersion;
	
	@NaturalId
	@Column(name = "BB_MULTIMEDIA_ID", nullable = false)
	private final long bbMultimediaId;
	
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
	
	@ManyToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "multimedias")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<Session> sessions = new HashSet<Session>(0);
	
    @SuppressWarnings("unused")
	private MultimediaImpl() {
        this.bbMultimediaId = -1;
        this.entityVersion = -1;
        this.multimediaId = -1;
        this.creator = null;
        this.description = null;
        this.size = -1;
    }
    
    MultimediaImpl(long bbMultimediaId, ConferenceUser creator) {
        Validate.notNull(creator, "creator cannot be null");
        this.multimediaId = -1;
        this.entityVersion = -1;
        this.size = -1;
        this.bbMultimediaId = bbMultimediaId;
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
	public DateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(DateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public long getMultimediaId() {
		return multimediaId;
	}

	@Override
	public long getBbMultimediaId() {
		return bbMultimediaId;
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
		return "MultimediaImpl [multimediaId=" + multimediaId
				+ ", bbMultimediaId=" + bbMultimediaId + ", creator=" + creator
				+ ", description=" + description + ", size=" + size
				+ ", lastUpdated=" + lastUpdated + ", sessions=" + sessions
				+ "]";
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (bbMultimediaId ^ (bbMultimediaId >>> 32));
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
        MultimediaImpl other = (MultimediaImpl) obj;
        if (bbMultimediaId != other.bbMultimediaId)
            return false;
        return true;
    }
}
