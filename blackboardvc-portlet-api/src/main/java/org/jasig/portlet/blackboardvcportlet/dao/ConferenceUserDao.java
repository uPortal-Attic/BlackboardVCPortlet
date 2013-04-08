package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Map;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;

public interface ConferenceUserDao {
    Set<Session> getChairedSessionsForUser(ConferenceUser user);

    Set<Session> getNonChairedSessionsForUser(ConferenceUser user);
    
    ConferenceUser createBlackboardUser(String email, String displayName);

    ConferenceUser updateBlackboardUser(ConferenceUser user);

    void deleteBlackboardUser(ConferenceUser message);
    
    ConferenceUser getUser(long userId);

    ConferenceUser getBlackboardUser(String email);

    Set<ConferenceUser> findAllMatchingUsers(String email, Map<String, String> attributes);
    
    //TODO add API for getting chaired and nonChaired sessions for a user

}