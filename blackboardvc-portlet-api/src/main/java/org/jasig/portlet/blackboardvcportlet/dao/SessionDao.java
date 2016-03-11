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
package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;

import com.elluminate.sas.BlackboardSessionResponse;

public interface SessionDao {
    Set<ConferenceUser> getSessionChairs(Session session);
    
    Set<ConferenceUser> getSessionNonChairs(Session session);
    
    Set<SessionRecording> getSessionRecordings(Session session);
    
    Set<SessionTelephony> getSessionTelephony(Session session);
    
    Set<Session> getAllSessions();
    
    Session getSession(long sessionId);
    
    Session getSessionByBlackboardId(long bbSessionId);
    
    Session createSession(BlackboardSessionResponse sessionResponse, String guestUrl);
    
    Session updateSession(BlackboardSessionResponse sessionResponse);
    
    void deleteSession(Session session);

    Session addMultimediaToSession(Session session, Multimedia multimedia);

	Session deleteMultimediaFromSession(Session session, Multimedia multimedia);

	Set<Multimedia> getSessionMultimedias(Session session);

	Session addPresentationToSession(Session session, Presentation presentation);

	Session removePresentationFromSession(Session session);

	void clearSessionUserList(long sessionId, boolean isChairList);

}
