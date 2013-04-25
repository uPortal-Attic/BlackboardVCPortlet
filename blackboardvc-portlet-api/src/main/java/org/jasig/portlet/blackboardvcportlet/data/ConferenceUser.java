package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Set;

public interface ConferenceUser extends Serializable {
    public static final String EXTERNAL_USERID_PREFIX = "@EXT@";

    long getUserId();
    
    /**
     * Globally unique identifier for the user in the portal, if this
     * field is null the user is external
     */
    String getUniqueId();
    
    String getBlackboardUniqueId();
    
    /**
     * If the user is an external user, if true {@link #getInvitationKey()} will return a value
     */
    boolean isExternal();

    /**
     * Email address to use for notifications for the user
     */
    String getEmail();
    
    /**
     * Set the current email address for the user, cannot be null
     */
    void setEmail(String email);
    
    /**
     * Additional email addresses the user has been invited to sessions with
     */
    Set<String> getAdditionalEmails();
    
    /**
     * Name displayed in the UI and in BlackBoard
     */
    String getDisplayName();
    
    /**
     * Set the current display name for the user, cannot be null
     */
    void setDisplayName(String displayName);
    
    /**
     * Optional unique id for a user invite, null if {@link #isExternal()} is false
     */
    String getInvitationKey();
}