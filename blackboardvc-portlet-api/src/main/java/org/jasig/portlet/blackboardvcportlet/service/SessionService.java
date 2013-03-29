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

import java.util.List;

import javax.portlet.PortletPreferences;

import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionMultimedia;
import org.jasig.portlet.blackboardvcportlet.data.SessionPresentation;
import org.jasig.portlet.blackboardvcportlet.data.SessionUrl;
import org.jasig.portlet.blackboardvcportlet.data.SessionUrlId;
import org.jasig.portlet.blackboardvcportlet.data.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service class for manipulating Collaborate sessions and their persistent
 * Entities
 *
 * @author rgood
 */

public interface SessionService
{
	public List<Session> getSessionsForUser(String uid);

	public Session getSession(long sessionId);

	public SessionUrl getSessionUrl(SessionUrlId sessionUrlId);

	public void deleteSession(long sessionId) throws Exception;

	public List<Session> getAllSessions();

	public void createEditSession(Session session, PortletPreferences prefs, List<User> extParticipantList) throws Exception;

	public User getExtParticipant(long sessionId, String email);

	public void storeSession(Session session);

	public void notifyModerators(User creator, Session session, List<User> users, String launchUrl) throws Exception;

	public void notifyOfDeletion(Session session) throws Exception;

	public void notifyIntParticipants(User creator, Session session, List<User> users, String launchUrl) throws Exception;

	public void notifyExtParticipants(User creator, Session session, List<User> users) throws Exception;

	public void addExtParticipant(User user, long sessionId);

	public void deleteExtParticipants(long sessionId);

	public SessionPresentation getSessionPresentation(String sessionId);

	public void deleteSessionPresentation(long sessionId, long presentationId) throws Exception;

	public void addSessionPresentation(String uid, long sessionId, MultipartFile file) throws Exception;

	public void deleteSessionMultimedia(long sessionId) throws Exception;

	public void deleteSessionMultimediaFiles(long sessionId, String[] multimediaIds) throws Exception;

	public List<SessionMultimedia> getSessionMultimedia(long sessionId);

	public void addSessionMultimedia(String uid, long sessionId, MultipartFile file) throws Exception;
}
