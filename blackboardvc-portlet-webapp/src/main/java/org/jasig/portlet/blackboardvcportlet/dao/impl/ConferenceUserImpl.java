package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.UserSessionUrl;

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
    @Column(name="EMAIL", length = 500, nullable = false)
    private final String email;
    
    @Column(name="DISPLAY_NAME", length = 500)
    private String displayName;
    
    @ElementCollection
    @MapKeyColumn(name="ATTR_NAME")
    @Column(name="ATTR_VALUE", nullable = false)
    @CollectionTable(
            name="VC2_USER_ATTR", 
            joinColumns=@JoinColumn(name="USER_ID"),
            uniqueConstraints=@UniqueConstraint(columnNames={"USER_ID", "ATTR_NAME", "ATTR_VALUE"}))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Map<String, String> attributes = new HashMap<String, String>(0);
    
    @OneToMany(targetEntity = MultimediaImpl.class, fetch = FetchType.LAZY, mappedBy = "creator")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<Multimedia> multimedias = new HashSet<Multimedia>(0);

    @OneToMany(targetEntity = PresentationImpl.class, fetch = FetchType.LAZY, mappedBy = "creator")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<Presentation> presentations = new HashSet<Presentation>(0);

    @OneToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "creator", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<Session> ownedSessions = new HashSet<Session>(0);

    @ManyToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "chairs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<Session> chairedSessions = new HashSet<Session>(0);
    
    @ManyToMany(targetEntity = SessionImpl.class, fetch = FetchType.LAZY, mappedBy = "nonChairs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private final Set<Session> nonChairedSessions = new HashSet<Session>(0);

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
        this.email = null;
    }

    ConferenceUserImpl(String email, String displayName) {
        Validate.notNull(email, "email for user cannot be null");
        
        this.userId = -1;
        this.entityVersion = -1;
        this.email = email;
        this.displayName = displayName;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
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
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    Set<Session> getOwnedSessions() {
        return ownedSessions;
    }

    Set<Session> getChairedSessions() {
        return chairedSessions;
    }

    Set<Session> getNonChairedSessions() {
        return nonChairedSessions;
    }
    
    Set<Multimedia> getMultimedias() {
    	return multimedias;
    }
    
    Set<Presentation> getPresentations() {
    	return presentations;
    }

    @Override
    public String toString() {
        return "BlackboardUser [userId=" + userId + ", entityVersion=" + entityVersion + ", displayName=" + displayName + ", email=" + email + ", attributes=" + attributes + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
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
        if (email == null) {
            if (other.email != null)
                return false;
        }
        else if (!email.equals(other.email))
            return false;
        return true;
    }
}
