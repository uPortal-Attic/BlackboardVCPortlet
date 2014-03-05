package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;

import com.elluminate.sas.BlackboardSessionTelephonyResponse;

/**
 * Save/read/update session telephony information
 * @author levett
 * @since 2.0.0
 */
public interface SessionTelephonyDao {
	Set<SessionTelephony> getAllTelephony();
    
	SessionTelephony getSessionTelephony(long telephonyId);
    
    void updateSessionTelephony(SessionTelephony telephony);
    
    void deleteTelephony(long sessionId);

	SessionTelephony createOrUpdateTelephony(BlackboardSessionTelephonyResponse telephonyResponse);

	SessionTelephony getSessionTelephony(Session session);

}
