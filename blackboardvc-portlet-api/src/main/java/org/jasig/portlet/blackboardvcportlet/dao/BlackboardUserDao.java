package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Map;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.BlackboardSession;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;

public interface BlackboardUserDao {
    Set<BlackboardSession> getChairedSessionsForUser(long userId);

    Set<BlackboardSession> getNonChairedSessionsForUser(long userId);
    
    BlackboardUser createBlackboardUser(String email, String displayName);

    BlackboardUser updateBlackboardUser(BlackboardUser user);

    void deleteBlackboardUser(BlackboardUser message);
    
    BlackboardUser getBlackboardUser(long userId);

    BlackboardUser getBlackboardUser(String email);

    Set<BlackboardUser> findAllMatchingUsers(String email, Map<String, String> attributes);
    
    //TODO add API for getting chaired and nonChaired sessions for a user

}