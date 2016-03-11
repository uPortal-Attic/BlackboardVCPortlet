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

public interface ConferenceUserDao {
    Set<Session> getOwnedSessionsForUser(ConferenceUser user);
    
    Set<Session> getChairedSessionsForUser(ConferenceUser user);

    Set<Session> getNonChairedSessionsForUser(ConferenceUser user);
    
    Set<Multimedia> getMultimediasForUser(ConferenceUser user);
    
    Set<Presentation> getPresentationsForUser(ConferenceUser user);
    
    ConferenceUser createInternalUser(String uniqueId);
    
    ConferenceUser createExternalUser(String displayName, String email);
    
    ConferenceUser createExternalUser(String email);

    ConferenceUser updateUser(ConferenceUser user);

    void deleteUser(ConferenceUser message);
    
    Set<ConferenceUser> getUsers(long... userIds);
    
    ConferenceUser getUser(long userId);
    
    ConferenceUser getUserByUniqueId(String uniqueId);

    ConferenceUser getExternalUserByEmail(String email);
    
    Set<ConferenceUser> getUsersByPrimaryEmail(String email);
    
    Set<ConferenceUser> getUsersByAnyEmail(String email);
}