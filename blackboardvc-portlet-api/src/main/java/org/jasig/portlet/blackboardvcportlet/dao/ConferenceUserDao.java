package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Map;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;

public interface ConferenceUserDao {
    Set<Session> getOwnedSessionsForUser(ConferenceUser user);
    
    Set<Session> getChairedSessionsForUser(ConferenceUser user);

    Set<Session> getNonChairedSessionsForUser(ConferenceUser user);
    
    Set<Multimedia> getMultimediasForUser(ConferenceUser user);
    
    ConferenceUser createUser(String email, String displayName);

    ConferenceUser updateUser(ConferenceUser user);

    void deleteUser(ConferenceUser message);
    
    ConferenceUser getUser(long userId);

    ConferenceUser getUser(String email);

    Set<ConferenceUser> findAllMatchingUsers(String email, Map<String, String> attributes);

	Set<Presentation> getPresentationsForUser(ConferenceUser user);
    
    //TODO add API for getting chaired and nonChaired sessions for a user

}