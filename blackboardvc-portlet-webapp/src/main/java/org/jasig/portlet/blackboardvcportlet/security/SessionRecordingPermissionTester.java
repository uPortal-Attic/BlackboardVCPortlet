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
package org.jasig.portlet.blackboardvcportlet.security;

import java.io.Serializable;

import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Various permission checks for a {@link SessionRecording}
 * 
 * @author Eric Dalquist
 */
@Service
public class SessionRecordingPermissionTester implements PermissionTester<SessionRecording> {
    private SessionPermissionTester sessionPermissionTester;
    private SessionRecordingDao sessionRecordingDao;

    @Autowired
    public void setSessionPermissionTester(SessionPermissionTester sessionPermissionTester) {
        this.sessionPermissionTester = sessionPermissionTester;
    }

    @Autowired
    public void setSessionRecordingDao(SessionRecordingDao sessionRecordingDao) {
        this.sessionRecordingDao = sessionRecordingDao;
    }

    @Override
    public Class<SessionRecording> getDomainObjectType() {
        return SessionRecording.class;
    }

    @Override
    public boolean hasPermission(ConferenceUser user, SessionRecording sessionRecording, Object permission) {
        final Session session = sessionRecording.getSession();
        return this.sessionPermissionTester.hasPermission(user, session, permission);
    }
    
    @Override
    public boolean hasPermissionById(ConferenceUser user, Serializable targetId, Object permission) {
        final SessionRecording sessionRecording = this.sessionRecordingDao.getSessionRecording((Long)targetId);
        return this.hasPermission(user, sessionRecording, permission);
    }
}
