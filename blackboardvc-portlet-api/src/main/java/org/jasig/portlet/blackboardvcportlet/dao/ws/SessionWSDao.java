package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;

import com.elluminate.sas.BlackboardSessionResponse;

public interface SessionWSDao {
	
	//create
	public BlackboardSessionResponse createSession(ConferenceUser user, SessionForm sessionForm, boolean fullAccess);
	public String buildSessionUrl(long sessionId, String displayName);
	//TODO : this might just go into create session
	public void createSessionTelephony(int sessionId, Object telephony);

	//read
	//TODO : Determine if this is needed as we should be reading from the local database.
	public void getSessions(int userId, int groupingId, int sessionId, int creatorId, Object startTime, Object endTime, String sessionName);
	
	//Misc. reads that have to do with a session but don't warrant there own dao
	public List getSessionAttendance(int sessionId, Object startTime);
	public List getSessionTelephony(int sessionId);
	
	//update
	public void updateSession(Object session);
	
	//delete
	public void deleteSession(int sessionId);
	public void clearSessionChairList(int sessionId);
	public void clearSessionNonChairList(int sessionId);
	
	
}
