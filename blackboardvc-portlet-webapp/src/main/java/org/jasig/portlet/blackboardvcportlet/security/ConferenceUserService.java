package org.jasig.portlet.blackboardvcportlet.security;

import org.jasig.portlet.blackboardvcportlet.data.BasicUser;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.springframework.security.core.Authentication;

/**
 * Provides access to the currently authenticated user
 * 
 * @author Eric Dalquist
 */
public interface ConferenceUserService {
    /**
     * @return The currently authenticated user, null if no user is currently authenticated
     */
    ConferenceUser getCurrentConferenceUser();
    
    /**
     * @return The user for the specified {@link Authentication}
     */
    ConferenceUser getConferenceUser(Authentication authentication);
    
    /**
     * @return The current authentication object
     */
    Authentication getCurrentAuthentication();
    
    ConferenceUser getOrCreateConferenceUser(String displayName, String email);
    
    ConferenceUser getOrCreateConferenceUser(String uniqueId, String displayName, String email);
    
    ConferenceUser getOrCreateConferenceUser(BasicUser basicUser);
}
