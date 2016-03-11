/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
