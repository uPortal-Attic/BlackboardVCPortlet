package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;


import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;
import org.jasig.portlet.blackboardvcportlet.validations.annotations.PhoneNumber;

public class TelephonyForm implements SessionTelephony {
	
	public TelephonyForm(long sessionId) {
		this.sessionId = sessionId;
	}
	
	public TelephonyForm() {
		
	}

	private static final long serialVersionUID = 1L;
	
	private String chairPhone;
	
	private String chairPIN;
	private String nonChairPhone;
	private String nonChairPIN;
	private boolean isPhone;
	private String sessionSIPPhone;
	private String sessionPIN;
	
	private long sessionId;
	
	@NotBlank
	@PhoneNumber
	public String getChairPhone() {
		return chairPhone;
	}
	
	public void setChairPhone(String chairPhone) {
		this.chairPhone = chairPhone;
	}
	
	@Pattern(regexp="[0-9\\*\\#\\,]*", message="{org.jasig.portlet.blackboardvcportlet.validations.pin.defaultmessage}")
	@Length(max=64)
	public String getChairPIN() {
		return chairPIN;
	}
	public void setChairPIN(String chairPIN) {
		this.chairPIN = chairPIN;
	}
	
	@PhoneNumber
	@NotBlank
	public String getNonChairPhone() {
		return nonChairPhone;
	}
	public void setNonChairPhone(String nonChairPhone) {
		this.nonChairPhone = nonChairPhone;
	}
	
	@Pattern(regexp="[0-9\\*\\#\\,]*", message="{org.jasig.portlet.blackboardvcportlet.validations.pin.defaultmessage}")
	@Length(max=64)
	public String getNonChairPIN() {
		return nonChairPIN;
	}
	public void setNonChairPIN(String nonChairPIN) {
		this.nonChairPIN = nonChairPIN;
	}
	public boolean isPhone() {
		return isPhone;
	}
	public void setPhone(boolean isPhone) {
		this.isPhone = isPhone;
	}
	
	@NotBlank
	@Pattern(regexp="^sip\\:\\S{1,}", message="{org.jasig.portlet.blackboardvcportlet.validations.sipphone.defaultmessage}")
	public String getSessionSIPPhone() {
		return sessionSIPPhone;
	}
	
	/**
	 * Sets the variable sessionSIPPhone. If setting to "" it will change it to null so that the validation
	 * will only provide one error message
	 * @param sessionSIPPhone
	 */
	public void setSessionSIPPhone(String sessionSIPPhone) {
		this.sessionSIPPhone = StringUtils.defaultIfEmpty(sessionSIPPhone, null);
	}
	public String getSessionPIN() {
		return sessionPIN;
	}
	
	@Pattern(regexp="[0-9\\*\\#\\,]*", message="{org.jasig.portlet.blackboardvcportlet.validations.pin.defaultmessage}")
	@Length(max=64)
	public void setSessionPIN(String sessionPIN) {
		this.sessionPIN = sessionPIN;
	}
	@Override
	public Session getSession() {
		return null;
	}
	@Override
	public long getTelephonyId() {
		return 0;
	}
	
	@Override
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
}
