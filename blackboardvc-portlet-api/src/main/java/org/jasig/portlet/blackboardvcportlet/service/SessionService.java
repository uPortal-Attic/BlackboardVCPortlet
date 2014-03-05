/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.service;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser.Roles;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;
import org.springframework.web.multipart.MultipartFile;


/**
 * Service class for manipulating Collaborate sessions and their persistent
 * Entities
 *
 * @author rgood
 */

public interface SessionService {
    
    /**
     * Get a session for the user and sessionId. This method throws an exception if the
     * specified user does not have access to view the session
     */
    Session getSession(long sessionId);
    
    /**
     * Creates or Updates a session for the user, form and access flag. This method throws an exception
     * if the specified user doesn't have access to edit the specified session
     */
    Session createOrUpdateSession(ConferenceUser creator, SessionForm sessionForm);
    
    void removeSession(long sessionId);
    
    Set<Multimedia> getSessionMultimedia(Session session);
    

    
    Set<ConferenceUser> getSessionChairs(Session session);

    ConferenceUser addSessionChair(long sessionId, String displayName, String email);
    
    ConferenceUser addSessionChair(long sessionId, long userId);
    
    ConferenceUser updateRole(long sessionId, long userId, Roles chair);
    
    void removeSessionChairs(long sessionId, Iterable<ConferenceUser> user);
    
    void removeSessionChairs(long sessionId, long... userIds);
    public void removeSessionChairs(long sessionId, Iterable<ConferenceUser> users, boolean sendCancelEmail);
    
    
    
    Set<ConferenceUser> getSessionNonChairs(Session session);

    ConferenceUser addSessionNonChair(long sessionId, String displayName, String email);
    
    ConferenceUser addSessionNonChair(long sessionId, long userId);
    
    void removeSessionNonChairs(long sessionId, Iterable<ConferenceUser> user);
    
    void removeSessionNonChairs(long sessionId, long... userIds);

    
    
    void addPresentation(long sessionId, MultipartFile file);
    
    void deletePresentation(long sessionId);
    
    void addMultimedia(long sessionId, MultipartFile file);
    
    void deleteMultimedia(long sessionId, long... multimediaIds);

	public String getOrCreateSessionUrl(ConferenceUser user, Session session);

	boolean isSessionParticipant(Session session, ConferenceUser user);

	Set<Session> getAllSessions();

	void populateLaunchUrl(ConferenceUser user, Session session);

	void removeSessionNonChairs(long sessionId, Iterable<ConferenceUser> users, boolean sendEmail);

	void removeSessionNonChairs(long sessionId, boolean sendEmail, long ... userIds);

	ConferenceUser addSessionChair(long sessionId, long userId, boolean sendEmail);

	void removeSessionChairs(long sessionId, boolean sendEmail, long... userIds);

	String getOrCreateSessionUrl(ConferenceUser user, Session session,
			boolean forceFetch);

	void deleteSessionUrl(ConferenceUser user, Session session);
	
	/**
	 * @param session The session of which you are querying upon
	 * @return SessionTelephony object, or null if one doesn't exist
	 */
	SessionTelephony getSessionTelephony(Session session);
	
	void createOrUpdateSessionTelephony(long sessionId, SessionTelephony telephony);
	
	void deleteSessionTelephony(long sessionId);
}
