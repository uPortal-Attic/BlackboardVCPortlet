package org.jasig.portlet.blackboardvcportlet.service.impl;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceSecurityUser;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        
        final String email = principal.getEmail();
        return this.conferenceUserDao.getUser(email);
    }

    @Override
    public ConferenceUser getOrCreateConferenceUser(String email, String displayName) {
        final ConferenceUser user = this.conferenceUserDao.getUser(email);
        if (user != null) {
            return user;
        }
        
        return this.conferenceUserDao.createUser(email, displayName);
    }
}
