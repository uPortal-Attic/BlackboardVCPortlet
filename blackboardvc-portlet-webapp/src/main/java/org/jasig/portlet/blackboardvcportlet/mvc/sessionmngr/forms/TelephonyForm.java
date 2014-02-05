package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms;


import org.hibernate.validator.constraints.NotBlank;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;

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
	public String getChairPhone() {
		return chairPhone;
	}
	
	public void setChairPhone(String chairPhone) {
		this.chairPhone = chairPhone;
	}
	public String getChairPIN() {
		return chairPIN;
	}
	public void setChairPIN(String chairPIN) {
		this.chairPIN = chairPIN;
	}
	public String getNonChairPhone() {
		return nonChairPhone;
	}
	public void setNonChairPhone(String nonChairPhone) {
		this.nonChairPhone = nonChairPhone;
	}
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
	public String getSessionSIPPhone() {
		return sessionSIPPhone;
	}
	public void setSessionSIPPhone(String sessionSIPPhone) {
		this.sessionSIPPhone = sessionSIPPhone;
	}
	public String getSessionPIN() {
		return sessionPIN;
	}
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
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
}
