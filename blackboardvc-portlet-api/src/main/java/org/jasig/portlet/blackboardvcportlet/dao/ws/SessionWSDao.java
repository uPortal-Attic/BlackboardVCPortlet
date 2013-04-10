package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

public interface SessionWSDao {
	
	//create
	public void buildSessionUrl(int sessionId, String displayName, String uid);
	public void createSession(Object session);
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
