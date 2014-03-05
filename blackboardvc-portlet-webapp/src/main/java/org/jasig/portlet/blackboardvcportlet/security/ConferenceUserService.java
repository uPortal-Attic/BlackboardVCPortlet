package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.BasicUser;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.service.ToManyResultsException;
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
    
    /**
     * @param uniqueId The {@link BasicUser#getUniqueId()} for the user to find
     * @return The user for the uniqueId
     */
    BasicUser findBasicUser(String uniqueId);

    /**
     * Search for a set of users via {@link BasicUser#getDisplayName()}. It is up to 
     * the implementation to parse the name parameter to return the best result set based
     * on the name or name fragment.
     *  
     * 
     * @param name The name or name fragment to search on.
     * @return
     * @throws ToManyResultsException If more results than the implementation's limit are found, used to prevent using this service to spider available users 
     */
    Set<BasicUser> searchForBasicUserByName(String name);

    /**
     * Search for a set of users via {@link BasicUser#getEmail()}. It is up to 
     * the implementation to parse the email parameter to return the best result set based
     * on the email or email fragment.
     *  
     * 
     * @param email The email or email fragment to search on.
     * @return
     * @throws ToManyResultsException If more results than the implementation's limit are found, used to prevent using this service to spider available users
     */
    Set<BasicUser> searchForBasicUserByEmail(String email);
}
