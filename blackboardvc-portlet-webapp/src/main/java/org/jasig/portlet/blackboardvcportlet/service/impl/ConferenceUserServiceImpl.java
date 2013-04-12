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
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        
        return getConferenceUser(authentication);
    }

    public ConferenceUser getConferenceUser(Authentication authentication) {
        final ConferenceSecurityUser principal = (ConferenceSecurityUser)authentication.getPrincipal();
        if (principal == null) {
            return null;
        }
        
        final String email = principal.getEmail();
        return this.conferenceUserDao.getUser(email);
    }
}
