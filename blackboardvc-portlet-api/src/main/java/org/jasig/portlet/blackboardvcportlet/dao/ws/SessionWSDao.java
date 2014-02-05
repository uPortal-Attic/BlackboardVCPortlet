package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;

import com.elluminate.sas.BlackboardSessionAttendanceResponse;
import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSessionTelephonyResponse;
import com.elluminate.sas.BlackboardSetSessionTelephony;

public interface SessionWSDao {
	
	//create
	public BlackboardSessionResponse createSession(ConferenceUser user, SessionForm sessionForm);
	public String buildGuestSessionUrl(long sessionId);
	public String buildSessionUrl(long sessionId, ConferenceUser user);
	public BlackboardSessionTelephonyResponse createSessionTelephony(long sessionId, SessionTelephony telephony);

	//read
	//TODO : Determine if this is needed as we should be reading from the local database.
	/**
	 * TODO : Determine if this is needed as we should be reading from the local database.
	 * Must specify at least one, can have many
	 * @param userId
	 * @param groupingId
	 * @param sessionId
	 * @param creatorId
	 * @param startTime
	 * @param endTime
	 * @param sessionName
	 * @return
	 */
	public List<BlackboardSessionResponse> getSessions(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);
	
	//Misc. reads that have to do with a session but don't warrant there own dao
	public List<BlackboardSessionAttendanceResponse> getSessionAttendance(long sessionId, Object startTime);
	public List<BlackboardSessionTelephonyResponse> getSessionTelephony(long sessionId);
	public boolean removeSessionTelephony(long sessionId);
	
	//update
	public BlackboardSessionResponse updateSession(long bbSessionId, SessionForm sessionForm);
	public BlackboardSessionResponse setSessionChairs(long bbSessionId, Set<ConferenceUser> sessionChairs);
	public BlackboardSessionResponse setSessionNonChairs(long bbSessionId, Set<ConferenceUser> sessionNonChairs);
	
	//delete
	public boolean deleteSession(long sessionId);
	public boolean clearSessionChairList(long sessionId);
	public boolean clearSessionNonChairList(long sessionId);
	
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations);
	

	

	
	
}
