package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;

public interface ConferenceUserDao {
    Set<Session> getOwnedSessionsForUser(ConferenceUser user);
    
    Set<Session> getChairedSessionsForUser(ConferenceUser user);

    Set<Session> getNonChairedSessionsForUser(ConferenceUser user);
    
    Set<Multimedia> getMultimediasForUser(ConferenceUser user);
    
    Set<Presentation> getPresentationsForUser(ConferenceUser user);
    
    ConferenceUser createInternalUser(String uniqueId);
    
    ConferenceUser createExternalUser(String displayName, String email);
    
    ConferenceUser createExternalUser(String email);

    ConferenceUser updateUser(ConferenceUser user);

    void deleteUser(ConferenceUser message);
    
    Set<ConferenceUser> getUsers(long... userIds);
    
    ConferenceUser getUser(long userId);
    
    ConferenceUser getUserByUniqueId(String uniqueId);

    ConferenceUser getExternalUserByEmail(String email);
    
    Set<ConferenceUser> getUsersByPrimaryEmail(String email);
    
    Set<ConferenceUser> getUsersByAnyEmail(String email);
}