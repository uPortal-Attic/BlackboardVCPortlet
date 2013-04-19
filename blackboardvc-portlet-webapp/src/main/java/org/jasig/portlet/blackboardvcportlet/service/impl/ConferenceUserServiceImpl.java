package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceSecurityUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
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
        
        return this.conferenceUserDao.createExternalUser(displayName, email);
    }
}
