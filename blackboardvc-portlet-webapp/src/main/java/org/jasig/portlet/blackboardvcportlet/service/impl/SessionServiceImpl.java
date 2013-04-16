package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.dao.MultimediaDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.MultimediaWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.PresentationWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.RecordingWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;
import org.springframework.ws.client.WebServiceClientException;

import com.elluminate.sas.BlackboardMultimediaResponse;
import com.elluminate.sas.BlackboardSessionResponse;

@Service
public class SessionServiceImpl implements SessionService, ServletContextAware {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ConferenceUserService conferenceUserService;
	private ConferenceUserDao conferenceUserDao;
    private SessionDao sessionDao;
    private MultimediaDao multimediaDao;
	private SessionWSDao sessionWSDao;
	private MultimediaWSDao multimediaWSDao;
	private PresentationWSDao presentationWSDao;
	private RecordingWSDao recordingWSDao;
	private File tempDir;
	
	@Autowired
	public void setMultimediaDao(MultimediaDao multimediaDao) {
        this.multimediaDao = multimediaDao;
    }

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

    @Autowired
	public void setMultimediaWSDao(MultimediaWSDao multimediaWSDao) {
        this.multimediaWSDao = multimediaWSDao;
    }

    @Autowired
    public void setPresentationWSDao(PresentationWSDao presentationWSDao) {
        this.presentationWSDao = presentationWSDao;
    }

    @Autowired
    public void setRecordingWSDao(RecordingWSDao recordingWSDao) {
        this.recordingWSDao = recordingWSDao;
    }
    
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.tempDir = WebUtils.getTempDir(servletContext);
    }

    /**
	 * A user needs "edit" to view the set of session chairs but we don't want the call to fail
	 * if they only have "view" permission. So we pre-auth them with view and then filter all
	 * the results unless they have "edit"
	 */
    @Override
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#session, 'view')")
	@PostFilter("hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')")
    public Set<ConferenceUser> getSessionChairs(Session session) {
        return new LinkedHashSet<ConferenceUser>(sessionDao.getSessionChairs(session));
    }

    /**
     * A user needs "edit" to view the set of session non chairs but we don't want the call to fail
     * if they only have "view" permission. So we pre-auth them with view and then filter all
     * the results unless they have "edit"
     */
    @Override
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#session, 'view')")
    @PostFilter("hasRole('ROLE_ADMIN') || hasPermission(#session, 'edit')")
    public Set<ConferenceUser> getSessionNonChairs(Session session) {
        return new LinkedHashSet<ConferenceUser>(sessionDao.getSessionNonChairs(session));
    }

    /**
     * A user needs "edit" and ROLE_FULL_ACCESS to view the set of session multemedia
     * but we don't want the call to fail if they only have "view" permission. So we
     * pre-auth them with view and then filter all the results unless they have ROLE_FULL_ACCESS and "edit"
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#session, 'view')")
    @PostFilter("hasRole('ROLE_ADMIN') || (hasRole('ROLE_FULL_ACCESS') && hasPermission(#session, 'edit'))")
    public Set<Multimedia> getSessionMultimedia(Session session) {
        return new LinkedHashSet<Multimedia>(sessionDao.getSessionMultimedias(session));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'view')")
    public Session getSession(long sessionId) {
        return this.sessionDao.getSession(sessionId);
    }

    /*
     * Not rolling back for WS related exceptions so the work done "so far" is still persisted to the database in
     * an attempt to keep the WS and DB layers in sync 
     */
    @Override
    @Transactional(noRollbackFor = { WebServiceClientException.class, XmlMappingException.class })
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'edit')")
    public void removeSession(long sessionId) {
        final Session session = this.sessionDao.getSession(sessionId);
        
        final Set<Multimedia> multimedias = this.sessionDao.getSessionMultimedias(session);
        for (final Multimedia multimedia : multimedias) {
            //Un-link multimedia file from session
            this.multimediaWSDao.removeSessionMultimedia(session.getBbSessionId(), multimedia.getBbMultimediaId());
            this.sessionDao.deleteMultimediaFromSession(sessionId, multimedia);
            
            //Delete multimedia file from repository
            this.multimediaWSDao.removeRepositoryMultimedia(multimedia.getBbMultimediaId());
            this.multimediaDao.deleteMultimedia(multimedia);
        }
        
        //TODO delete presentation
        
        this.sessionWSDao.deleteSession(session.getBbSessionId());
        this.sessionDao.deleteSession(session);
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

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_FULL_ACCESS') && hasPermission(#sessionId, 'org.jasig.portlet.blackboardvcportlet.data.Session', 'edit'))")
    public void addMultimedia(long sessionId, MultipartFile file) {
        final Session session = this.sessionDao.getSession(sessionId);
        final ConferenceUser conferenceUser = this.conferenceUserService.getCurrentConferenceUser();
        
        final BlackboardMultimediaResponse multimediaResponse = createSessionMultimedia(session, conferenceUser, file);
        
        //Add Multimedia object to local DB
        final String filename = FilenameUtils.getName(file.getOriginalFilename());
        final Multimedia multimedia = this.multimediaDao.createMultimedia(multimediaResponse, filename);
        
        //Associate Multimedia with session
        this.sessionDao.addMultimediaToSession(session.getBbSessionId(), multimedia);
    }

    private BlackboardMultimediaResponse createSessionMultimedia(Session session, ConferenceUser conferenceUser, MultipartFile file) {
        final String filename = FilenameUtils.getName(file.getOriginalFilename());
        
        File multimediaFile = null;
        try {
            //Transfer the uploaded file to our own temp file so we can use a FileDataSource
            multimediaFile = File.createTempFile(filename, ".tmp", this.tempDir);
            file.transferTo(multimediaFile);
            
            //Upload the file to BB
            return this.multimediaWSDao.createSessionMultimedia(
                session.getBbSessionId(), 
                conferenceUser.getEmail(), 
                filename, 
                "",
                new DataHandler(new FileDataSource(multimediaFile)));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to upload multimedia file '" + filename + "'", e);
        }
        finally {
            FileUtils.deleteQuietly(multimediaFile);
        }
    }
}