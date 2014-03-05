package org.jasig.portlet.blackboardvcportlet.data;


public interface ConferenceUser extends BasicUser {
	
	public static enum Roles {
		CHAIR,
		NONCHAIR
	}
	
	
    public static final String EXTERNAL_USERID_PREFIX = "@EXT@";

    long getUserId();
    
    String getBlackboardUniqueId();
    
    /**
     * If the user is an external user, if true {@link #getInvitationKey()} will return a value
     */
    boolean isExternal();
    
    /**
     * Set the current email address for the user, cannot be null
     */
    void setEmail(String email);
    
    /**
     * Set the current display name for the user, cannot be null
     */
    void setDisplayName(String displayName);
    
    /**
     * Optional unique id for a user invite, null if {@link #isExternal()} is false
     */
    String getInvitationKey();
}