package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.BlackboardSession;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;

import com.elluminate.sas.SessionResponse;

public interface BlackboardSessionDao {
    Set<BlackboardUser> getSessionChairs(long sessionId);
    
    Set<BlackboardUser> getSessionNonChairs(long sessionId);
    
    BlackboardSession getSession(long sessionId);
    
    BlackboardSession getSessionByBlackboardId(long blackboardId);
    
    BlackboardSession createSession(SessionResponse sessionResponse);
    
    BlackboardSession updateSession(SessionResponse sessionResponse);
    
    void deleteSession(BlackboardSession session);
}
