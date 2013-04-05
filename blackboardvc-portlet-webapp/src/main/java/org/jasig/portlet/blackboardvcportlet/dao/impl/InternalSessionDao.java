package org.jasig.portlet.blackboardvcportlet.dao.impl;

import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;

interface InternalSessionDao extends SessionDao {
    SessionImpl getSession(long sessionId);
    
    SessionImpl getSessionByBlackboardId(long bbSessionId);

}
