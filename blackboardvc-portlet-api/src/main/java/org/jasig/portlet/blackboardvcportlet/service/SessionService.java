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

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;


/**
 * Service class for manipulating Collaborate sessions and their persistent
 * Entities
 *
 * @author rgood
 */

public interface SessionService {
    /**
     * Creates or Updates a session for the user, form and access flag. This method throws an exception
     * if the specified user doesn't have access to edit the specified session
     */
    void createOrUpdateSession(ConferenceUser user, SessionForm sessionForm);
    
    /**
     * Get a session for the user and sessionId. This method throws an exception if the
     * specified user does not have access to view the session
     */
    Session getSession(ConferenceUser user, long sessionId);
    
    
//	public Set<BlackboardSession> getSessionsForUser(String uid);
//
//	public BlackboardSession getSession(long sessionId);
//
//	public void deleteSession(long sessionId) throws Exception;
//
//	public Set<BlackboardSession> getAllSessions();
//
//	public void createEditSession(BlackboardSession session, PortletPreferences prefs, List<BlackboardUser> extParticipantList) throws Exception;
//
//	public void storeSession(BlackboardSession session);
//
//	public void notifyModerators(BlackboardUser creator, BlackboardSession session, List<BlackboardUser> users, String launchUrl) throws Exception;
//
//	public void notifyOfDeletion(BlackboardSession session) throws Exception;
//
//	public void notifyIntParticipants(BlackboardUser creator, BlackboardSession session, List<BlackboardUser> users, String launchUrl) throws Exception;
//
//	public void notifyExtParticipants(BlackboardUser creator, BlackboardSession session, List<BlackboardUser> users) throws Exception;
//
//	public void addExtParticipant(BlackboardUser user, long sessionId);
//
//	public void deleteExtParticipants(long sessionId);
//
//	public SessionPresentation getSessionPresentation(String sessionId);
//
//	public void deleteSessionPresentation(long sessionId, long presentationId) throws Exception;
//
//	public void addSessionPresentation(String uid, long sessionId, MultipartFile file) throws Exception;
//
//	public void deleteSessionMultimedia(long sessionId) throws Exception;
//
//	public void deleteSessionMultimediaFiles(long sessionId, String[] multimediaIds) throws Exception;
//
//	public List<SessionMultimedia> getSessionMultimedia(long sessionId);
//
//	public void addSessionMultimedia(String uid, long sessionId, MultipartFile file) throws Exception;
}
