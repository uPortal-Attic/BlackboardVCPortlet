package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elluminate.sas.BlackboardSessionResponse;

@Service
public class SessionServiceImpl implements SessionService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ConferenceUserService conferenceUserService;
	private ConferenceUserDao conferenceUserDao;
    private SessionDao sessionDao;
	private SessionWSDao sessionWSDao;
	
	@Autowired
	public void setConferenceUserDao(ConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }

    @Autowired
	public void setConferenceUserService(ConferenceUserService conferenceUserService) {
        this.conferenceUserService = conferenceUserService;
    }

	@Autowired
    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

	@Autowired
	public void setSessionWSDao(SessionWSDao value) {
		this.sessionWSDao = value;
	}

	/**
	 * A user needs "edit" to view the set of session chairs but we don't want the call to fail
	 * if they only have "view" permission. So we pre-auth them with view and then filter all
	 * the results unless they have "edit"
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#session, 'view')")
	@PostFilter("hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')")
    public Set<ConferenceUser> getSessionChairs(Session session) {
        return new LinkedHashSet<ConferenceUser>(sessionDao.getSessionChairs(session));
    }

    /**
     * A user needs "edit" to view the set of session chairs but we don't want the call to fail
     * if they only have "view" permission. So we pre-auth them with view and then filter all
     * the results unless they have "edit"
     */
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#session, 'view')")
    @PostFilter("hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')")
    public Set<ConferenceUser> getSessionNonChairs(Session session) {
        return new LinkedHashSet<ConferenceUser>(sessionDao.getSessionNonChairs(session));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'view')")
    public Session getSession(long sessionId) {
        return this.sessionDao.getSession(sessionId);
    }

    @Override
    @Transactional
    @PreAuthorize("#sessionForm.newSession || hasRole('ROLE_ADMIN') || hasPermission(#sessionForm.sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'edit')")
    public void createOrUpdateSession(ConferenceUser user, SessionForm sessionForm) {
        if (sessionForm.isNewSession()) {
            final BlackboardSessionResponse sessionResponse = sessionWSDao.createSession(user, sessionForm);
            final String guestUrl = sessionWSDao.buildSessionUrl(sessionResponse.getSessionId(), "GUEST_PLACEHOLDER");
        	
        	//Remove guest username so that guest user's are prompted when they use the URL
            sessionDao.createSession(sessionResponse, guestUrl.replace("&username=GUEST_PLACEHOLDER", ""));
        }
        else {
            final Session session = sessionDao.getSession(sessionForm.getSessionId());
            final BlackboardSessionResponse sessionResponse = sessionWSDao.updateSession(session.getBbSessionId(), sessionForm);
            sessionDao.updateSession(sessionResponse);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'edit')")
    public void addSessionChair(long sessionId, String displayName, String email) {
        final ConferenceUser newSessionChair = this.conferenceUserService.getOrCreateConferenceUser(email, displayName);
        
        final Session session = this.sessionDao.getSession(sessionId);
        final Set<ConferenceUser> sessionChairs = new LinkedHashSet<ConferenceUser>(this.getSessionChairs(session));
        sessionChairs.add(newSessionChair);
        
        final BlackboardSessionResponse sessionResponse = this.sessionWSDao.setSessionChairs(session.getBbSessionId(), sessionChairs);
        sessionDao.updateSession(sessionResponse);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'edit')")
    public void removeSessionChairs(long sessionId, String... emails) {
        final Session session = this.sessionDao.getSession(sessionId);
        final Set<ConferenceUser> sessionChairs = new LinkedHashSet<ConferenceUser>(this.getSessionChairs(session));
        
        for (final String email : emails) {
            final ConferenceUser user = conferenceUserDao.getUser(email);
            if (user != null) {
                sessionChairs.remove(user);
            }
        }
        
        final BlackboardSessionResponse sessionResponse = this.sessionWSDao.setSessionChairs(session.getBbSessionId(), sessionChairs);
        sessionDao.updateSession(sessionResponse);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'edit')")
    public void addSessionNonChair(long sessionId, String displayName, String email) {
        final ConferenceUser newSessionNonChair = this.conferenceUserService.getOrCreateConferenceUser(email, displayName);
        
        final Session session = this.sessionDao.getSession(sessionId);
        final Set<ConferenceUser> sessionNonChairs = new LinkedHashSet<ConferenceUser>(this.getSessionNonChairs(session));
        sessionNonChairs.add(newSessionNonChair);
        
        final BlackboardSessionResponse sessionResponse = this.sessionWSDao.setSessionNonChairs(session.getBbSessionId(), sessionNonChairs);
        sessionDao.updateSession(sessionResponse);
        
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'edit')")
    public void removeSessionNonChairs(long sessionId, String... emails) {
        final Session session = this.sessionDao.getSession(sessionId);
        final Set<ConferenceUser> sessionNonChairs = new LinkedHashSet<ConferenceUser>(this.getSessionNonChairs(session));
        
        for (final String email : emails) {
            final ConferenceUser user = conferenceUserDao.getUser(email);
            if (user != null) {
                sessionNonChairs.remove(user);
            }
        }
        
        final BlackboardSessionResponse sessionResponse = this.sessionWSDao.setSessionNonChairs(session.getBbSessionId(), sessionNonChairs);
        sessionDao.updateSession(sessionResponse);
    }
}