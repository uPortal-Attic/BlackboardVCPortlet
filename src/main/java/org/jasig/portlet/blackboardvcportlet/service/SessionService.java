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

import com.elluminate.sas.BasicAuth;
import com.elluminate.sas.MultimediaResponse;
import com.elluminate.sas.PresentationResponse;
import com.elluminate.sas.SASDefaultAdapter;
import com.elluminate.sas.SASDefaultAdapterV3Port;
import com.elluminate.sas.SessionResponse;
import freemarker.template.utility.StringUtil;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.portlet.PortletPreferences;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionExtParticipantDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionMultimediaDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionPresentationDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionUrlDao;
import org.jasig.portlet.blackboardvcportlet.data.RecordingShort;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionExtParticipant;
import org.jasig.portlet.blackboardvcportlet.data.SessionExtParticipantId;
import org.jasig.portlet.blackboardvcportlet.data.SessionMultimedia;
import org.jasig.portlet.blackboardvcportlet.data.SessionPresentation;
import org.jasig.portlet.blackboardvcportlet.data.SessionUrl;
import org.jasig.portlet.blackboardvcportlet.data.SessionUrlId;
import org.jasig.portlet.blackboardvcportlet.data.User;

/**
 * Service class for manipulating Collaborate sessions and their persistent
 * Entities
 *
 * @author rgood
 */
@Service
public class SessionService {

    protected final Log logger = LogFactory.getLog(SessionService.class);
    private boolean isInit = false;
    private BasicAuth user;
    @Autowired
    SessionDao sessionDao;
    @Autowired
    SessionUrlDao sessionUrlDao;
    @Autowired
    MailTemplateService mailTemplateService;
    @Autowired
    LdapService ldapService;
    @Autowired
    RecordingService recordingService;
    @Autowired
    SessionExtParticipantDao sessionExtParticipantDao;
    @Autowired
    SessionPresentationDao sessionPresentationDao;
    @Autowired
    SessionMultimediaDao sessionMultimediaDao;

    public List<Session> getSessionsForUser(String uid) {
        List<Session> sessionList = sessionDao.getSessionsForUser(uid);
        for (int i = 0; i < sessionList.size(); i++) {
            
            if ((sessionList.get(i).getChairList()!=null&&sessionList.get(i).getChairList().indexOf(uid+",") != -1)||(sessionList.get(i).getCreatorId().equals(uid))||(sessionList.get(i).getChairList()!=null&&sessionList.get(i).getChairList().endsWith(uid)))
            {
                sessionList.get(i).setCurrUserCanEdit(true);
            } 
            else 
            {
                sessionList.get(i).setCurrUserCanEdit(false);
            }
        }
        return sessionList;
    }

    public Session getSession(long sessionId) {
        logger.debug("getSession called");
        return sessionDao.getSession(sessionId);
    }

    public SessionUrl getSessionUrl(SessionUrlId sessionUrlId, PortletPreferences prefs) {
        // Guest url uses user id set to -1 from the DB
        if (sessionUrlId.getUserId() == null) {
            sessionUrlId.setUserId("-1");
        }
        try {
            SessionUrl sessionUrl = sessionUrlDao.getSessionUrl(sessionUrlId);
            if (sessionUrl != null) {
                logger.debug("found session URL in DB");
                return sessionUrl;
            }
        } catch (Exception e) {
            logger.debug(e);

        }
        SessionUrl sessionUrl = new SessionUrl();
        sessionUrl.setDisplayName(sessionUrlId.getDisplayName());
        sessionUrl.setSessionId(sessionUrlId.getSessionId());
        if (!sessionUrlId.getUserId().equals("-1")) {
            sessionUrl.setUserId(sessionUrlId.getUserId());
        }

        if (!this.isInit()) {
            doInit(prefs);
        }

        try {
            logger.debug("getting session url from Collaborate");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());
            String buildSessionUrl = port.buildSessionUrl(sessionUrl.getSessionId(), sessionUrl.getDisplayName(), sessionUrl.getUserId());
            sessionUrl.setUrl(buildSessionUrl);
            sessionUrl.setLastUpdated(new Date());
            if (sessionUrl.getUserId() == null) {
                sessionUrl.setUserId("-1");
            }
            sessionUrl.setLastUpdated(new Date());
            sessionUrlDao.saveSessionUrl(sessionUrl);

        } catch (Exception e) {
            logger.error("Error getting/storing session URL", e);
        }

        return sessionUrl;
    }

    public void deleteSession(long sessionId, PortletPreferences prefs) throws Exception {
        logger.debug("deleteSession called for :" + sessionId);
        if (!this.isInit()) {
            doInit(prefs);
        }     

        try { 
            
            Session session = sessionDao.getSession(sessionId);
            
            // Call Web Service Operation
            logger.debug("Setup session web service call");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());
            
            logger.debug("deleting session multimedia");
            deleteSessionMultimedia(prefs, sessionId);

            SessionPresentation sessionPresentation = getSessionPresentation(Long.toString(sessionId));

            if (sessionPresentation != null) {
                logger.debug("deleting session presentation");
                deleteSessionPresentation(prefs, sessionId, sessionPresentation.getPresentationId());
            }
            
            
            logger.debug("Calling removeSession:" + sessionId);
            try {
                boolean removeSession = port.removeSession(sessionId);
                logger.debug("removeSession called, returned:" + removeSession);
            } catch (Exception e) {
                logger.error(e);
            }
            
            logger.debug("Deleting session urls");
            sessionUrlDao.deleteSessionUrls(sessionId);
            logger.debug("Finished deleting session urls");

            logger.debug("Now deleting session");
            sessionDao.deleteSession(sessionId);
            logger.debug("Finished deleting session");
            
            notifyOfDeletion(session);
            
            logger.debug("Deleting session ext participants");
            sessionExtParticipantDao.deleteAllExtParticipants(sessionId);
            logger.debug("Finished deleting ext participants");
            

        } catch (Exception ex) {
            logger.error(ex.toString());
            throw ex;
        }

    }

    public List<Session> getAllSessions() {
        logger.debug("getAllSessions called");
        List<Session> sessions = sessionDao.getAllSesssions();
        for (int i = 0; i < sessions.size(); i++) {
            sessions.get(i).setCurrUserCanEdit(true);
        }
        return sessions;
    }

    public void createEditSession(Session session, PortletPreferences prefs, List<User> extParticipantList) throws Exception {
        if (!this.isInit()) {
            doInit(prefs);
        }

        try { // Call Web Service Operation
            logger.debug("Setup session web service call");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());
            logger.debug("Calling setSession:" + session.getSessionId());
            List<SessionResponse> setSessionResponse;
            if (session.getSessionId() > 0) {
                logger.debug("Existing session, calling updateSession");
                setSessionResponse = port.updateSession(session.getSessionId(), session.getStartTime().getTime(), session.getEndTime().getTime(), session.getSessionName(), session.getAccessType(), session.getBoundaryTime(), session.getChairList(), session.getChairNotes(), session.getGroupingList(), session.getMaxTalkers(), session.getMaxCameras(), session.isMustBeSupervised(), session.getNonChairList(), session.getNonChairNotes(), session.isOpenChair(), session.isPermissionsOn(), session.isRaiseHandOnEnter(), session.getRecordingModeType(), null, null, session.getReserveSeats(), session.isSecureSignOn(), null, session.isAllowInSessionInvites(), session.isHideParticipantNames());
            } else {
                logger.debug("New session, calling setSession");
                setSessionResponse = port.setSession(session.getCreatorId(), session.getStartTime().getTime(), session.getEndTime().getTime(), session.getSessionName(), session.getAccessType(), session.getBoundaryTime(), session.getChairList(), session.getChairNotes(), null, session.getMaxTalkers(), session.getMaxCameras(), session.isMustBeSupervised(), session.getNonChairList(), session.getNonChairNotes(), session.isOpenChair(), session.isPermissionsOn(), session.isRaiseHandOnEnter(), session.getRecordingModeType(), null, null, session.getReserveSeats(), session.isSecureSignOn(), null, session.isAllowInSessionInvites(), session.isHideParticipantNames());
                logger.debug("setSession called, recieved response");

            }

            for (int i = 0; i < setSessionResponse.size(); i++) {
                logger.debug("Setting sessionId");
                session.setSessionId(setSessionResponse.get(i).getSessionId());
                session.setLastUpdated(new Date());
                logger.debug("Storing session");
                this.storeSession(session);
                logger.debug("Session stored");
            }

            logger.debug("Update recordings associated with session");
            List<RecordingShort> recordings = recordingService.getRecordingsForSession(session.getSessionId());
            if (recordings != null) {
                boolean changed;
                for (int i = 0; i < recordings.size(); i++) {
                    changed=false;
                    if ((recordings.get(i).getChairList()==null&&session.getChairList()!=null)
                            ||(session.getChairList()==null&&recordings.get(i).getChairList()!=null)
                            ||(!recordings.get(i).getChairList().equals(session.getChairList()))) 
                    {
                        changed=true;
                        recordings.get(i).setChairList(session.getChairList());
                    }
                    if ((recordings.get(i).getNonChairList()==null&&session.getNonChairList()!=null)
                            ||(session.getNonChairList()==null&&recordings.get(i).getNonChairList()!=null)
                            ||(!recordings.get(i).getNonChairList().equals(session.getNonChairList()))) 
                    {
                        changed=true;
                        recordings.get(i).setNonChairList(session.getNonChairList());
                    }
                    if (changed) 
                    {
                        logger.debug("Saving updated recording short");
                        recordingService.saveRecordingShort(recordings.get(i));
                    }

                }
            }


            logger.debug("Finished updating recordings");

            this.deleteExtParticipants(session.getSessionId());
            for (int i = 0; i < extParticipantList.size(); i++) {
                this.addExtParticipant(extParticipantList.get(i), session.getSessionId());
            }

            String callBackUrl = prefs.getValue("callbackUrl", null);
            logger.debug("Setting callback Url to:" + callBackUrl);
            if (callBackUrl != null) {
                boolean setApiCallbackUrl = port.setApiCallbackUrl(callBackUrl);
                logger.debug("callBackUrl response:" + setApiCallbackUrl);
            }

        } catch (Exception ex) {
            logger.error(ex.toString());
            throw ex;
        }
    }

    public User getExtParticipant(long sessionId, String email) {
        SessionExtParticipantId sessionExtParticipantId = new SessionExtParticipantId();
        sessionExtParticipantId.setSessionId(sessionId);
        sessionExtParticipantId.setParticipantEmail(email);

        SessionExtParticipant sessionExtParticipant = sessionExtParticipantDao.getSessionExtParticipant(sessionExtParticipantId);

        User extParticipant = new User();
        extParticipant.setUid(email);
        extParticipant.setEmail(email);

        if (sessionExtParticipant != null) {
            extParticipant.setDisplayName(sessionExtParticipant.getDisplay_name());
        }

        return extParticipant;
    }

    public boolean isInit() {
        return this.isInit;
    }

    public void doInit(PortletPreferences prefs) {
        logger.debug("doInit called");
        user = new BasicAuth();
        user.setName(prefs.getValue("wsusername", null));
        user.setPassword(prefs.getValue("wspassword", null));
        isInit = true;
    }

    public void storeSession(Session session) {
        sessionDao.saveSession(session);
    }

    public void notifyModerators(PortletPreferences prefs, User creator, Session session, List<User> users,String launchUrl) throws Exception {
        logger.debug("notifyModerators called");
        String[] substitutions;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String creatorDetails = creator.getDisplayName() + " (" + creator.getEmail() + ")";
       
        List<String> toList;
        for (int i = 0; i < users.size(); i++) {
            logger.debug("user name:" + users.get(i).getDisplayName());
            logger.debug("user email:" + users.get(i).getDisplayName());

            toList = new ArrayList<String>();
            toList.add(users.get(i).getEmail());

            substitutions = new String[]{users.get(i).getDisplayName(), creatorDetails, session.getSessionName(), dateFormat.format(session.getStartTime()), dateFormat.format(session.getEndTime()), launchUrl, creatorDetails};
            mailTemplateService.sendEmailUsingTemplate(creator.getEmail(), toList, null, substitutions, "moderatorMailMessage");
        }
        logger.debug("finished");
    }
    
    public void notifyOfDeletion(Session session) throws Exception {
        logger.debug("notifyOfDeletion called");
        String[] substitutions;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        
        User creator = ldapService.getUserDetails(session.getCreatorId());
        String creatorDetails= "Unknown user";
        
        if (creator!=null)
        {
            creatorDetails = creator.getDisplayName() + " (" + creator.getEmail() + ")";
            logger.debug("creatorDetails set:"+creatorDetails);
        }
             
        List<User> users = new ArrayList<User>();
        
        User lookupUser;     
        
        logger.debug("Finished initialisation of creator and variables");
        if (session.getChairList()!=null&&!session.getChairList().equals(""))
        {
                logger.debug("Adding chair list users");
                String[] chairList = StringUtil.split(session.getChairList(),',');
                
                for (int i=0;i<chairList.length;i++)
                {
                    lookupUser=ldapService.getUserDetails(chairList[i]);
                    if (lookupUser!=null)
                    {
                        users.add(lookupUser);
                    }
                   
                }
              
        }
            
        if (session.getNonChairList()!=null&&!session.getNonChairList().equals(""))
        {
                logger.debug("Adding nonchair list users");
                String[] nonChairList = StringUtil.split(session.getNonChairList(),',');
                             
                for (int i=0;i<nonChairList.length;i++)
                {
                    lookupUser=ldapService.getUserDetails(nonChairList[i]);
                    if (lookupUser!=null)
                    {
                        users.add(lookupUser);
                    }
                    else
                    {
                        lookupUser = this.getExtParticipant(session.getSessionId(),nonChairList[i]);
                        if (lookupUser==null)
                        {
                            lookupUser = new User();
                            lookupUser.setEmail(nonChairList[i]);
                        }
                        
                        users.add(lookupUser);
                    }
                }
                          
        }
        
        List<String> toList;
        for (int i = 0; i < users.size(); i++) {
            logger.debug("user name:" + users.get(i).getDisplayName());
            logger.debug("user email:" + users.get(i).getDisplayName());

            toList = new ArrayList<String>();
            toList.add(users.get(i).getEmail());

            substitutions = new String[]{users.get(i).getDisplayName(), creatorDetails, session.getSessionName(), dateFormat.format(session.getStartTime()), dateFormat.format(session.getEndTime()), creatorDetails};
            mailTemplateService.sendEmailUsingTemplate(creator.getEmail(), toList, null, substitutions, "sessionDeletionMessage");
        }
        logger.debug("finished");
    }

    public void notifyIntParticipants(PortletPreferences prefs, User creator, Session session, List<User> users, String launchUrl) throws Exception {
        logger.debug("notifyIntParticipants called");
        String[] substitutions;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String creatorDetails = creator.getDisplayName() + " (" + creator.getEmail() + ")";

        List<String> toList;
        for (int i = 0; i < users.size(); i++) {
            toList = new ArrayList<String>();
            toList.add(users.get(i).getEmail());
            substitutions = new String[]{users.get(i).getDisplayName(), creatorDetails, session.getSessionName(), dateFormat.format(session.getStartTime()), dateFormat.format(session.getEndTime()), launchUrl, creatorDetails};
            mailTemplateService.sendEmailUsingTemplate(creator.getEmail(), toList, null, substitutions, "intParticipantMailMessage");
        }
        logger.debug("finished");
    }

    public void notifyExtParticipants(PortletPreferences prefs, User creator, Session session, List<User> users) throws Exception {
        logger.debug("notifyExtParticipants called");
        String[] substitutions;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String creatorDetails = creator.getDisplayName() + " (" + creator.getEmail() + ")";
        SessionUrl sessionUrl;
        SessionUrlId sessionUrlId;
        List<String> toList;
        // Get the guest launch URL
        sessionUrlId = new SessionUrlId();
        sessionUrlId.setSessionId(session.getSessionId());
        sessionUrlId.setDisplayName("Guest");
        sessionUrl = this.getSessionUrl(sessionUrlId, prefs);       
        String extParticipantUrl;
        for (int i = 0; i < users.size(); i++) {
            toList = new ArrayList<String>();
            toList.add(users.get(i).getEmail());
            extParticipantUrl = sessionUrl.getUrl().replaceFirst("Guest",URLEncoder.encode(users.get(i).getDisplayName(), "UTF-8"));
            substitutions = new String[]{users.get(i).getDisplayName(), creatorDetails, session.getSessionName(), dateFormat.format(session.getStartTime()), dateFormat.format(session.getEndTime()), extParticipantUrl, creatorDetails};
            mailTemplateService.sendEmailUsingTemplate(creator.getEmail(), toList, null, substitutions, "extParticipantMailMessage");
        }
        logger.debug("finished");
    }

    public void addExtParticipant(User user, long sessionId) {
        logger.debug("addExtParticipant called for session,user: (" + sessionId + "," + user.getEmail() + ")");
        SessionExtParticipantId sessionExtParticipantId = new SessionExtParticipantId();
        SessionExtParticipant sessionExtParticipant = new SessionExtParticipant();

        sessionExtParticipantId.setParticipantEmail(user.getEmail());
        sessionExtParticipantId.setSessionId(sessionId);

        sessionExtParticipant.setSessionExtParticipantId(sessionExtParticipantId);
        sessionExtParticipant.setDisplay_name(user.getDisplayName());
        sessionExtParticipantDao.storeSessionExtParticipant(sessionExtParticipant);
    }

    public void deleteExtParticipants(long sessionId) {
        logger.debug("deleteExtParticipants called for :" + sessionId);
        sessionExtParticipantDao.deleteAllExtParticipants(sessionId);
    }

    public SessionPresentation getSessionPresentation(String sessionId) {
        logger.debug("getSessionPresentation called");
        List<SessionPresentation> sessionPresentationList = sessionPresentationDao.getSessionPresentation(sessionId);

        if (sessionPresentationList != null && sessionPresentationList.size() > 0) {
            return sessionPresentationList.get(0);
        } else {
            logger.debug("getSessionPresentation is going to return null");
            return null;
        }
    }

    public void deleteSessionPresentation(PortletPreferences prefs, long sessionId, long presentationId) throws Exception {
        logger.debug("deleteSessionPresentation called");
        if (!this.isInit()) {
            doInit(prefs);
        }

        try {
            logger.debug("Setup session web service call");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());
            boolean removeSessionPresentation = port.removeSessionPresentation(sessionId, presentationId);
            logger.debug("removeSessionPresentation returned:" + removeSessionPresentation);
            boolean removeRepositoryPresentation = port.removeRepositoryPresentation(presentationId);
            logger.debug("removeRepositoryPresentation returned:" + removeRepositoryPresentation);
            sessionPresentationDao.deleteSessionPresentation(Long.toString(presentationId));
        } catch (Exception e) {
            logger.error("Exception caught deleting session presentation", e);
            throw e;
        }

    }

    public void addSessionPresentation(String uid, PortletPreferences prefs, long sessionId, MultipartFile file) throws Exception {
        logger.debug("addSessionPresentation called");
        if (!this.isInit()) {
            doInit(prefs);
        }

        try { // Call Web Service Operation
            logger.debug("Setup session web service call");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());
            ByteArrayDataSource rawData = new ByteArrayDataSource(file.getBytes(), file.getContentType());
            logger.debug("ByteArrayDataSource created");
            DataHandler dataHandler = new DataHandler(rawData);
            logger.debug("DataHandler created from ByteArrayDataSource");
            List<PresentationResponse> uploadRepositoryPresentation = port.uploadRepositoryPresentation(uid, file.getOriginalFilename(), null, dataHandler);
            logger.debug("uploadRepositoryPresentation called");


            if (uploadRepositoryPresentation != null) {
                SessionPresentation sessionPresentation = new SessionPresentation();
                sessionPresentation.setCreatorId(uid);
                sessionPresentation.setDateUploaded(new Date());
                sessionPresentation.setFileName(file.getOriginalFilename());
                sessionPresentation.setSessionId(Long.toString(sessionId));
                for (int i = 0; i < uploadRepositoryPresentation.size(); i++) {
                    boolean setSessionPresentation = port.setSessionPresentation(sessionId, uploadRepositoryPresentation.get(i).getPresentationId());
                    if (setSessionPresentation) {
                        sessionPresentation.setPresentationId(uploadRepositoryPresentation.get(i).getPresentationId());
                        sessionPresentationDao.storeSessionPresentation(sessionPresentation);
                    }
                }
            } else {
                logger.error("uploadRepositoryPresentation was null");
            }
        } catch (Exception e) {
            throw e;
        }


    }

    public void deleteSessionMultimedia(PortletPreferences prefs, long sessionId) throws Exception {
        logger.debug("deleteSessionMultimediaFiles called");
        List<SessionMultimedia> sessionMultimediaList = this.getSessionMultimedia(sessionId);
        if (!this.isInit()) {
            doInit(prefs);
        }

        try { // Call Web Service Operation
            logger.debug("Setup session web service call");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());
            for (int i = 0; i < sessionMultimediaList.size(); i++) {
                boolean removeSessionMultimedia = port.removeSessionMultimedia(sessionId,sessionMultimediaList.get(i).getMultimediaId());
                logger.debug("deleteSessionMultimedia returned:"+removeSessionMultimedia);
                boolean removeRepositoryMultimedia = port.removeRepositoryMultimedia(sessionMultimediaList.get(i).getMultimediaId());
                logger.debug("delete multimediaId (" + sessionMultimediaList.get(i).getMultimediaId() + " returned:" + removeRepositoryMultimedia);
                sessionMultimediaDao.deleteSessionMultimedia(sessionMultimediaList.get(i).getMultimediaId());
            }
        } catch (Exception e) {
            logger.error("Exception caught removing multimedia files", e);
            throw e;
        }
    }

    public void deleteSessionMultimediaFiles(PortletPreferences prefs, long sessionId, String[] multimediaIds) throws Exception {
        logger.debug("deleteSessionMultimediaFiles called");
        List<SessionMultimedia> sessionMultimediaList = this.getSessionMultimedia(sessionId);

        if (!this.isInit()) {
            doInit(prefs);
        }

        /* Call set session to remove the old ids, then remove them from
         the repository */
        try { // Call Web Service Operation
            logger.debug("Setup session web service call");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());

            for (int i = 0; i < multimediaIds.length; i++) {
                boolean removeSessionMultimedia = port.removeSessionMultimedia(sessionId, Long.valueOf(multimediaIds[i]));
                if (removeSessionMultimedia) {
                    boolean removeRepositoryMultimedia = port.removeRepositoryMultimedia(Long.valueOf(multimediaIds[i]));
                    logger.debug("delete multimediaId (" + multimediaIds[i] + " returned:" + removeRepositoryMultimedia);
                    sessionMultimediaDao.deleteSessionMultimedia(Long.valueOf(multimediaIds[i]));
                } else {
                    throw new Exception("Error deleting session multimedia.");
                }
            }
        } catch (Exception e) {
            logger.error("Exception caught deleting session multimedia", e);
            throw e;
        }

    }

    public List<SessionMultimedia> getSessionMultimedia(long sessionId) {
        return sessionMultimediaDao.getSessionMultimedia(Long.toString(sessionId));
    }

    public void addSessionMultimedia(String uid, PortletPreferences prefs, long sessionId, MultipartFile file) throws Exception {
        logger.debug("addSessionMultimedia called");
        if (!this.isInit()) {
            doInit(prefs);
        }

        try { // Call Web Service Operation
            logger.debug("Setup session web service call");
            SASDefaultAdapter service = new SASDefaultAdapter();
            SASDefaultAdapterV3Port port = service.getDefaultAdapterPort();
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user.getName());
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, user.getPassword());
            ByteArrayDataSource rawData = new ByteArrayDataSource(file.getBytes(), file.getContentType());
            logger.debug("ByteArrayDataSource created");
            DataHandler dataHandler = new DataHandler(rawData);
            logger.debug("DataHandler created from ByteArrayDataSource");
            List<MultimediaResponse> uploadRepositoryMultimedia = port.uploadRepositoryMultimedia(uid, file.getOriginalFilename(), null, dataHandler);


            logger.debug("uploadRepositoryMultimedia called");


            if (uploadRepositoryMultimedia != null) {
                SessionMultimedia sessionMultimedia = new SessionMultimedia();
                sessionMultimedia.setCreatorId(uid);
                sessionMultimedia.setDateUploaded(new Date());
                sessionMultimedia.setFileName(file.getOriginalFilename());
                sessionMultimedia.setSessionId(Long.toString(sessionId));
                List<SessionMultimedia> sessionMultimediaList = sessionMultimediaDao.getSessionMultimedia(Long.toString(sessionId));
                String multimediaIds = "";
                for (int x = 0; x < sessionMultimediaList.size(); x++) {
                    multimediaIds += sessionMultimediaList.get(x).getMultimediaId();
                    multimediaIds += ",";
                }

                for (int i = 0; i < uploadRepositoryMultimedia.size(); i++) {
                    sessionMultimedia.setMultimediaId(uploadRepositoryMultimedia.get(i).getMultimediaId());
                    multimediaIds += sessionMultimedia.getMultimediaId();
                    boolean setSessionPresentation = port.setSessionMultimedia(sessionId, multimediaIds);
                    if (setSessionPresentation) {
                        sessionMultimediaDao.saveSessionMultimedia(sessionMultimedia);
                    }
                }
            } else {
                logger.error("uploadRepositoryMultimedia was null");
            }
        } catch (Exception e) {
            throw e;
        }


    }
}
