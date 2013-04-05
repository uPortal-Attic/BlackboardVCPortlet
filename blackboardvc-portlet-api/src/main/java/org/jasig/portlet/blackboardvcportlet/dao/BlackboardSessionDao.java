package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.BlackboardSession;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;

import com.elluminate.sas.SessionResponse;

public interface BlackboardSessionDao {
    Set<BlackboardUser> getSessionChairs(long sessionId);
    
    Set<BlackboardUser> getSessionNonChairs(long sessionId);
    
    Set<SessionRecording> getSessionRecordings(long sessionId);
    
    Set<BlackboardSession> getAllSessions();
    
    BlackboardSession getSession(long sessionId);
    
    BlackboardSession getSessionByBlackboardId(long bbSessionId);
    
    BlackboardSession createSession(SessionResponse sessionResponse, String guestUrl);
    
    BlackboardSession updateSession(SessionResponse sessionResponse);
    
    void deleteSession(BlackboardSession session);
}
