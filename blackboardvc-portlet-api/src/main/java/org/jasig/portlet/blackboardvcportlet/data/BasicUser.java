package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Set;

public interface BasicUser extends Serializable {
    /**
     * Globally unique identifier for the user in the portal, if this
     * field is null the user is external
     */
    String getUniqueId();

    /**
     * Email address to use for notifications for the user
     */
    String getEmail();
    
    /**
     * Additional email addresses the user has been invited to sessions with
     */
    Set<String> getAdditionalEmails();
    
    /**
     * Name displayed in the UI and in BlackBoard
     */
    String getDisplayName();
}