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
