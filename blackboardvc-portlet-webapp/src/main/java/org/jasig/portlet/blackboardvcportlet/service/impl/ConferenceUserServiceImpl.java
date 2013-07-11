package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.data.BasicUser;
import org.jasig.portlet.blackboardvcportlet.data.BasicUserImpl;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceSecurityUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.jasig.portlet.blackboardvcportlet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Pulls the current user's email address out of spring-security and then does a lookup via the user dao
 */
@Service
public class ConferenceUserServiceImpl implements ConferenceUserService {
    private ConferenceUserDao conferenceUserDao;
    private UserService userService;

    @Autowired
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
            final BasicUser basicUser = this.userService.findUser(uniqueId);
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
}
