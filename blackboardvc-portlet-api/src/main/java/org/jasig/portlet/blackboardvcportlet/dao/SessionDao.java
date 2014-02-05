package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;

import com.elluminate.sas.BlackboardSessionResponse;

public interface SessionDao {
    Set<ConferenceUser> getSessionChairs(Session session);
    
    Set<ConferenceUser> getSessionNonChairs(Session session);
    
    Set<SessionRecording> getSessionRecordings(Session session);
    
    Set<SessionTelephony> getSessionTelephony(Session session);
    
    Set<Session> getAllSessions();
    
    Session getSession(long sessionId);
    
    Session getSessionByBlackboardId(long bbSessionId);
    
    Session createSession(BlackboardSessionResponse sessionResponse, String guestUrl);
    
    Session updateSession(BlackboardSessionResponse sessionResponse);
    
    void deleteSession(Session session);

    Session addMultimediaToSession(Session session, Multimedia multimedia);

	Session deleteMultimediaFromSession(Session session, Multimedia multimedia);

	Set<Multimedia> getSessionMultimedias(Session session);

	Session addPresentationToSession(Session session, Presentation presentation);

	Session removePresentationFromSession(Session session);

	void clearSessionUserList(long sessionId, boolean isChairList);

}
