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
import org.jasig.portlet.blackboardvcportlet.data.Session;


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
    void createOrUpdateSession(ConferenceUser creator, SessionForm sessionForm);
    
    Set<ConferenceUser> getSessionChairs(Session session);

    Set<ConferenceUser> getSessionNonChairs(Session session);
    
    void addSessionChair(long sessionId, ConferenceUser newSessionChair);
}
