package org.jasig.portlet.blackboardvcportlet.service;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.BasicUser;

/**
 * Directory of users that can use the service
 * 
 * @author Eric Dalquist
 */
public interface UserService {
    /**
     * @param uniqueId The {@link BasicUser#getUniqueId()} for the user to find
     * @return The user for the uniqueId
     */
    BasicUser findUser(String uniqueId);
    
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
    Set<BasicUser> searchForUserByName(String name);

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
    Set<BasicUser> searchForUserByEmail(String email);
}
