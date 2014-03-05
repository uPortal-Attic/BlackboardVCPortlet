package org.jasig.portlet.blackboardvcportlet.dao.impl;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;

/**
 * Extended user dao interface for use by other DAOs
 */
interface InternalConferenceUserDao extends ConferenceUserDao {
    ConferenceUserImpl getUser(long userId);
    
    ConferenceUserImpl getUserByUniqueId(String uniqueId);
    
    ConferenceUserImpl createInternalUser(String uniqueId);
    
    ConferenceUserImpl getExternalUserByEmail(String email);
    
    ConferenceUserImpl createExternalUser(String email);
}
