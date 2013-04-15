package org.jasig.portlet.blackboardvcportlet.security;

import java.io.Serializable;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Various permission checks for a {@link Session}
 * 
 * @author Eric Dalquist
 */
@Service
public class SessionPermissionTester implements PermissionTester<Session> {
    public static final String VIEW     = "view";
    public static final String EDIT     = "edit";  
    public static final String DELETE   = "delete";
    
    private SessionDao sessionDao;

    @Autowired
    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public Class<Session> getDomainObjectType() {
        return Session.class;
    }

    @Override
    public boolean hasPermission(ConferenceUser user, Session session, Object permission) {
        final String permissionStr = String.valueOf(permission);
        
        if (VIEW.equalsIgnoreCase(permissionStr)) {
            if (session.getCreator().equals(user)) {
                return true;
            }
            
            if (isChair(user, session)) {
                return true;
            }
            
            return isNonChair(user, session);
        }
        else if (EDIT.equalsIgnoreCase(permissionStr)) {
            if (session.getCreator().equals(user)) {
                return true;
            }
            
            return isChair(user, session);
        }
        else if (DELETE.equalsIgnoreCase(permissionStr)) {
            return session.getCreator().equals(user);
        }
        
        return false;
    }
    
    @Override
    public boolean hasPermissionById(ConferenceUser user, Serializable targetId, Object permission) {
        final Session session = this.sessionDao.getSession((Long)targetId);
        return hasPermission(user, session, permission);
    }

    private boolean isNonChair(ConferenceUser user, Session session) {
        final Set<ConferenceUser> sessionNonChairs = this.sessionDao.getSessionNonChairs(session);
        return sessionNonChairs.contains(user);
    }

    private boolean isChair(ConferenceUser user, Session session) {
        final Set<ConferenceUser> sessionChairs = this.sessionDao.getSessionChairs(session);
        return sessionChairs.contains(user);
    }
}
