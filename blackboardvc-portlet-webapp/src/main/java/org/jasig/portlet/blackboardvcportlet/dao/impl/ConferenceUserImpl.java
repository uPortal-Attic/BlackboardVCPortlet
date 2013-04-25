package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.UserSessionUrl;
import org.springframework.util.Assert;

@Entity
@Table(name = "VC2_USER")
@SequenceGenerator(
        name="VC2_USER_GEN",
        sequenceName="VC2_USER_SEQ",
        allocationSize=10
    )
@TableGenerator(
        name="VC2_USER_GEN",
        pkColumnValue="VC2_USER",
        allocationSize=10
    )
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConferenceUserImpl implements ConferenceUser {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "VC2_USER_GEN")
    @Column(name = "USER_ID")
    private final long userId;
    
    @Version
    @Column(name = "ENTITY_VERSION")
    private final long entityVersion;
    
    @NaturalId
    @Column(name="UNIQUE_ID", length = 500, nullable = false)
    private final String uniqueId;
    
    @NaturalId
    @Column(name="IS_EXTERNAL", nullable = false)
    private final boolean external;
    
    @Index(name="VC2_IDX__USER_INVITE")
    @Column(name="INVITE_KEY", length = 500)
    private final String invitationKey;
    
    @Column(name="DISPLAY_NAME", length = 500)
    private String displayName;

    @Index(name="VC2_IDX__USER_EMAIL")
    @Column(name="EMAIL", length = 500)
    private String email;

    @ElementCollection(fetch =FetchType.EAGER, targetClass = String.class)
    @JoinTable(
        name = "VC2_USER_EMAILS",
        joinColumns = @JoinColumn(name = "USER_ID")
    )
    @Column(name = "EMAIL", length=600)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Fetch(FetchMode.JOIN)
    private final Set<String> additionalEmails = new HashSet<String>(0);
    
    @OneToMany(targetEntity = MultimediaImpl.class, fetch = FetchType.LAZY, mappedBy = "creator")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<MultimediaImpl> multimedias = new HashSet<MultimediaImpl>(0);

    @OneToMany(targetEntity = PresentationImpl.class, fetch = FetchType.LAZY, mappedBy = "creator")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<PresentationImpl> presentations = new HashSet<PresentationImpl>(0);

    @OneToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "creator", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<SessionImpl> ownedSessions = new HashSet<SessionImpl>(0);

    @ManyToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "chairs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<SessionImpl> chairedSessions = new HashSet<SessionImpl>(0);
    
    @ManyToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "nonChairs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<SessionImpl> nonChairedSessions = new HashSet<SessionImpl>(0);

    //Exists only to allow cascading deletes, should NEVER be accessed by normal code
    @OneToMany(mappedBy = "user", targetEntity = UserSessionUrlImpl.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<UserSessionUrl> userUrls = new HashSet<UserSessionUrl>(0);
    
    /**
     * needed by hibernate
     */
    @SuppressWarnings("unused")
    private ConferenceUserImpl() {
        this.userId = -1;
        this.entityVersion = -1;

        this.uniqueId = null;
        this.email = null;
        this.external = false;
        this.invitationKey = null;
    }
    
    /**
     * Creates a new internal user
     */
    ConferenceUserImpl(String uniqueId) {
        Assert.notNull(uniqueId, "uniqueId cannot be null");
        
        this.userId = -1;
        this.entityVersion = -1;
        
        this.uniqueId = uniqueId;
        this.external = false;
        this.invitationKey = null;
    }
    
    /**
     * Creates a new external user
     */
    ConferenceUserImpl(String email, String invitationKey) {
        Assert.notNull(email, "email cannot be null");
        Assert.notNull(invitationKey, "invitationKey cannot be null");
        
        this.userId = -1;
        this.entityVersion = -1;
        
        this.uniqueId = email;
        this.email = email;
        this.external = true;
        this.invitationKey = invitationKey;
    }
    

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
    
    @Override
	public String getBlackboardUniqueId() {
    	
    	if (this.isExternal()) {
    		return ConferenceUser.EXTERNAL_USERID_PREFIX + getUniqueId();
        }
        return getUniqueId();
	}

	public boolean isExternal() {
        return external;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String getInvitationKey() {
        return this.invitationKey;
    }
    
    @Override
    public Set<String> getAdditionalEmails() {
        return this.additionalEmails;
    }

    Set<SessionImpl> getOwnedSessions() {
        return ownedSessions;
    }

    Set<SessionImpl> getChairedSessions() {
        return chairedSessions;
    }

    Set<SessionImpl> getNonChairedSessions() {
        return nonChairedSessions;
    }
    
    Set<MultimediaImpl> getMultimedias() {
    	return multimedias;
    }
    
    Set<PresentationImpl> getPresentations() {
    	return presentations;
    }

    @Override
    public String toString() {
        return "ConferenceUserImpl [userId=" + userId + ", uniqueId=" + uniqueId + ", displayName=" + displayName + ", email=" + email + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (external ? 1231 : 1237);
        result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
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
        ConferenceUserImpl other = (ConferenceUserImpl) obj;
        if (external != other.external)
            return false;
        if (uniqueId == null) {
            if (other.uniqueId != null)
                return false;
        }
        else if (!uniqueId.equals(other.uniqueId))
            return false;
        return true;
    }
}
