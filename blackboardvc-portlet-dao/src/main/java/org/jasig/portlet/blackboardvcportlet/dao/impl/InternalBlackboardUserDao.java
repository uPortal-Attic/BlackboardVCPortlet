package org.jasig.portlet.blackboardvcportlet.dao.impl;

import org.jasig.portlet.blackboardvcportlet.dao.BlackboardUserDao;

/**
 * Extended user dao interface for use by other DAOs
 */
interface InternalBlackboardUserDao extends BlackboardUserDao {
    BlackboardUserImpl getBlackboardUser(long userId);
    
    BlackboardUserImpl getOrCreateBlackboardUser(String email);
}
