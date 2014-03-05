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
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;

@Entity
@Table(name = "VC2_TELEPHONY")
@SequenceGenerator(
        name="VC2_TELEPHONY_GEN",
        sequenceName="VC2_TELEPHONY_SEQ",
        allocationSize=10
    )
@TableGenerator(
        name="VC2_TELEPHONY_GEN",
        pkColumnValue="VC2_TELEPHONY",
        allocationSize=10
    )
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SessionTelephonyImpl implements SessionTelephony {
	private static final long serialVersionUID = 1L;
    
    @Version
    @Column(name = "ENTITY_VERSION")
    private final long entityVersion;
    
    @Id
    @GeneratedValue(generator = "VC2_TELEPHONY_GEN")
    @Column(name = "TELEPHONY_ID")
    private final long telephonyId;
    
    @ManyToOne(targetEntity = SessionImpl.class, optional = false)
    @JoinColumn(name = "SESSION_ID", nullable = false)
    private final SessionImpl session;
    
    @Column(name = "CHAIR_PHONE")
    private String chairPhone;
    
    @Column(name = "CHAIR_PIN")
    private String chairPIN;
    
    @Column(name = "NON_CHAIR_PHONE")
    private String nonChairPhone;
    
    @Column(name = "NON_CHAIR_PIN")
    private String nonChairPIN;
    
    @Column(name = "IS_PHONE")
    private boolean isPhone;
    
    @Column(name = "SESSION_SIP_PHONE")
    private String sessionSIPPhone;
    
    @Column(name = "SESSION_PIN")
    private String sessionPIN;
    
    /**
     * Needed by hibernate
     */
    @SuppressWarnings("unused")
    private SessionTelephonyImpl() {
        this.entityVersion = -1;
        this.telephonyId = -1;
        this.session = null;
    }
    
    SessionTelephonyImpl(SessionImpl session) {
        this.entityVersion = -1;
        this.telephonyId = -1;
        this.session = session;
        
    }

    @Override
	public String getChairPhone() {
		return chairPhone;
	}

	public void setChairPhone(String chairPhone) {
		this.chairPhone = chairPhone;
	}

	@Override
	public String getChairPIN() {
		return chairPIN;
	}

	public void setChairPIN(String chairPIN) {
		this.chairPIN = chairPIN;
	}

	@Override
	public String getNonChairPhone() {
		return nonChairPhone;
	}

	public void setNonChairPhone(String nonChairPhone) {
		this.nonChairPhone = nonChairPhone;
	}

	@Override
	public String getNonChairPIN() {
		return nonChairPIN;
	}

	public void setNonChairPIN(String nonChairPIN) {
		this.nonChairPIN = nonChairPIN;
	}


	@Override
	public boolean isPhone() {
		return isPhone;
	}

	public void setPhone(boolean isPhone) {
		this.isPhone = isPhone;
	}

	@Override
	public String getSessionSIPPhone() {
		return sessionSIPPhone;
	}

	public void setSessionSIPPhone(String sessionSIPPhone) {
		this.sessionSIPPhone = sessionSIPPhone;
	}

	@Override
	public String getSessionPIN() {
		return sessionPIN;
	}

	public void setSessionPIN(String sessionPIN) {
		this.sessionPIN = sessionPIN;
	}

	@Override
	public SessionImpl getSession() {
		return session;
	}

	@Override
	public long getTelephonyId() {
		return telephonyId;
	}
}
