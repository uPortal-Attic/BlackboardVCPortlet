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
