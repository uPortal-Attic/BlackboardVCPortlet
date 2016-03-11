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
package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.data.BasicUser;
import org.jasig.portlet.blackboardvcportlet.data.BasicUserImpl;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceSecurityUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.jasig.portlet.blackboardvcportlet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;

/**
 * Pulls the current user's email address out of spring-security and then does a lookup via the user dao
 */
@Service
public class ConferenceUserServiceImpl implements ConferenceUserService {
    private static final Pattern NAME_NORMALIZER = Pattern.compile("\\s+");

    private ConferenceUserDao conferenceUserDao;
    private UserService userService;
    private Ehcache userServiceCache;
    
    @Autowired
    @Qualifier("userServiceCache")
    public void setEhcache(Ehcache ehcache) {
        this.userServiceCache = ehcache;
    }

    @Autowired(required=false)
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setConferenceUserDao(ConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }

    @Override
    public ConferenceUser getCurrentConferenceUser() {
        final Authentication authentication = getCurrentAuthentication();
        if (authentication == null) {
            return null;
        }
        
        return getConferenceUser(authentication);
    }

    @Override
    public Authentication getCurrentAuthentication() {
        final SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    @Override
    public ConferenceUser getConferenceUser(Authentication authentication) {
        final ConferenceSecurityUser principal = (ConferenceSecurityUser)authentication.getPrincipal();
        if (principal == null) {
            return null;
        }
        
        final String uniqueId = principal.getUniqueId();
        return this.conferenceUserDao.getUserByUniqueId(uniqueId);
    }

    @Override
    public ConferenceUser getOrCreateConferenceUser(String displayName, String email) {
        return getOrCreateConferenceUser(new BasicUserImpl(null, email, displayName));
    }

    @Override
    public ConferenceUser getOrCreateConferenceUser(String uniqueId, String displayName, String email) {
        
        if (uniqueId != null) {
            final BasicUser basicUser = this.findBasicUser(uniqueId);
            if (basicUser != null) {
                return getOrCreateConferenceUser(basicUser);
            }
        }
        
        return getOrCreateConferenceUser(new BasicUserImpl(null, email, displayName));
    }

    @Override
    public ConferenceUser getOrCreateConferenceUser(BasicUser basicUser) {
        //If the uniqueId is specified try to find an existing user by uniqueId
        //if no existing user is found create a new internal user
        final String uniqueId = basicUser.getUniqueId();
        if (uniqueId != null) {
            ConferenceUser user = this.conferenceUserDao.getUserByUniqueId(uniqueId);
            if (user != null) {
                return user;
            }
            
            user = this.conferenceUserDao.createInternalUser(uniqueId);
            user.setDisplayName(basicUser.getDisplayName());
            user.setEmail(basicUser.getEmail());
            user.getAdditionalEmails().addAll(basicUser.getAdditionalEmails());
            
            return user;
        }
        
        final String email = basicUser.getEmail();
        
        //Try to find a single user by email, it potentially possible to have more than one
        //user entry with the same primary email address in the case of a user being able to
        //chose their address
        Set<ConferenceUser> users = this.conferenceUserDao.getUsersByPrimaryEmail(email);
        if (users.size() == 1) {
            return DataAccessUtils.requiredSingleResult(users);
        }
        
        //If no users were found with that primary address try to find users that have the email
        //listed in their "additional" email address set
        if (users.isEmpty()) {
            users = this.conferenceUserDao.getUsersByAnyEmail(email);
            if (users.size() == 1) {
                return DataAccessUtils.requiredSingleResult(users);
            }
        }
        
        //Fall back to treating the user as external
        final ConferenceUser user = this.conferenceUserDao.getExternalUserByEmail(email);
        if (user != null) {
            return user;
        }
        
        return this.conferenceUserDao.createExternalUser(basicUser.getDisplayName(), email);
    }
    
    @Override
    public BasicUser findBasicUser(String uniqueId) {
        //First check for a user in the local DB
        final ConferenceUser confUser = this.conferenceUserDao.getUserByUniqueId(uniqueId);
        if (confUser != null) {
            return confUser;
        }

        //Then see if there is a UserService to look in
        if (this.userService == null) {
            return null;
        }

        //Check the local cache of UserService results
        final Serializable cacheKey = createUniqueIdCacheKey(uniqueId);
        final Element element = this.userServiceCache.get(cacheKey);
        if (element != null) {
            return (BasicUser)element.getObjectValue();
        }
        
        //Check the UserService
        final BasicUser basicUser = this.userService.findUser(uniqueId);
        
        //If there is a hit cache the result
        if (basicUser != null) {
            this.userServiceCache.put(new Element(cacheKey, basicUser));
        }
        
        return basicUser;
    }

    @Override
    public Set<BasicUser> searchForBasicUserByName(String name) {
        if (this.userService == null) {
            //TODO could fall back to a local db search here
            return Collections.emptySet();
        }
        
        //Normalize the name to try and get extra oomf out of the cache
        final String normalizedName = NAME_NORMALIZER.matcher(name.trim()).replaceAll(" ");
        
        //Check the local cache of results
        final Serializable cacheKey = createNameSearchCacheKey(normalizedName);
        final Element element = this.userServiceCache.get(cacheKey);
        if (element != null) {
            return (Set<BasicUser>)element.getObjectValue();
        }
        
        //No cache, perform the search
        final Set<BasicUser> result = this.userService.searchForUserByName(normalizedName);
        
        //Cache the search result
        this.userServiceCache.put(new Element(cacheKey, result));
        cacheUsers(result);
        
        return result;
    }

    @Override
    public Set<BasicUser> searchForBasicUserByEmail(String email) {
        if (this.userService == null) {
            //TODO could fall back to a local db search here
            return Collections.emptySet();
        }
        
        //Normalize the email to try and get extra oomf out of the cache
        final String normalizedEmail = email.trim();
        
        //Check the local cache of results
        final Serializable cacheKey = createEmailSearchCacheKey(normalizedEmail);
        final Element element = this.userServiceCache.get(cacheKey);
        if (element != null) {
            return (Set<BasicUser>)element.getObjectValue();
        }
        
        //No cache, perform the search
        final Set<BasicUser> result = this.userService.searchForUserByEmail(normalizedEmail);
        
        //Cache the search result
        this.userServiceCache.put(new Element(cacheKey, result));
        cacheUsers(result);
        
        return result;
    }


    private void cacheUsers(final Set<BasicUser> result) {
        for (final BasicUser user : result) {
            final Serializable userCacheKey = this.createUniqueIdCacheKey(user.getUniqueId());
            this.userServiceCache.put(new Element(userCacheKey, user));
        }
    }

    private Serializable createUniqueIdCacheKey(String uniqueId) {
        return ImmutableMap.of("type", "findByUniqueId", "uniqueId", uniqueId.toUpperCase());
    }

    private Serializable createNameSearchCacheKey(String name) {
        return ImmutableMap.of("type", "searchByName", "name", name.toUpperCase());
    }

    private Serializable createEmailSearchCacheKey(String email) {
        return ImmutableMap.of("type", "searchByEmail", "email", email.toUpperCase());
    }
}
