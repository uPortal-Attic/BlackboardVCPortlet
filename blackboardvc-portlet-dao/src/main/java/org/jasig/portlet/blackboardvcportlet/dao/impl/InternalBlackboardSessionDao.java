package org.jasig.portlet.blackboardvcportlet.dao.impl;

import org.jasig.portlet.blackboardvcportlet.dao.BlackboardSessionDao;

interface InternalBlackboardSessionDao extends BlackboardSessionDao {
    BlackboardSessionImpl getSession(long sessionId);
    
    BlackboardSessionImpl getSessionByBlackboardId(long bbSessionId);

}
