package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;

import com.elluminate.sas.BlackboardSessionAttendanceResponse;
import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSessionTelephonyResponse;
import com.elluminate.sas.BlackboardSetSessionTelephony;

public interface SessionWSDao {
	
	//create
	public BlackboardSessionResponse createSession(ConferenceUser user, SessionForm sessionForm);
	public String buildSessionUrl(Long sessionId, String displayName);
	//TODO : this might just go into create session
	public boolean createSessionTelephony(Long sessionId, BlackboardSetSessionTelephony telephony);

	//read
	//TODO : Determine if this is needed as we should be reading from the local database.
	public List<BlackboardSessionResponse> getSessions(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);
	
	//Misc. reads that have to do with a session but don't warrant there own dao
	public List<BlackboardSessionAttendanceResponse> getSessionAttendance(Long sessionId, Object startTime);
	public List<BlackboardSessionTelephonyResponse> getSessionTelephony(Long sessionId);
	
	//update
	public BlackboardSessionResponse updateSession(long bbSessionId, SessionForm sessionForm);
	public BlackboardSessionResponse setSessionChairs(long bbSessionId, Set<ConferenceUser> sessionChairs);
	
	//delete
	public boolean deleteSession(Long sessionId);
	public boolean clearSessionChairList(Long sessionId);
	public boolean clearSessionNonChairList(Long sessionId);

	

	
	
}
