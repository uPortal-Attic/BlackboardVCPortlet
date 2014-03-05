package org.jasig.portlet.blackboardvcportlet.service.util;

public enum MailSubstitutions {
	
	DISPLAY_NAME("displayName"),
	SESSION_BOUNDARY_TIME("boundaryTime"),
	SESSION_CREATOR_NAME("creatorName"),
	SESSION_CREATOR_EMAIL("creatorEmail"),
	SESSION_END_TIME("sessionEndTime"),
	SESSION_GUEST_URL("guestURL"),
	SESSION_NAME("sessionName"),
	SESSION_START_TIME("sessionStartTime"),
	SESSION_TYPE("sessionType"),
	SESSION_UPDATE_TEXT("sessionUpdateText"),
	SESSION_USER_URL("userURL");
	
	
	private String substitution;
	
	private MailSubstitutions(String substitution) {
		this.substitution = substitution;
	}

	public String getSubstitution() {
		return substitution;
	}

	public void setSubstitution(String substitution) {
		this.substitution = substitution;
	}
	
	@Override
	public String toString() {
		return substitution;
	}
	
	
}
