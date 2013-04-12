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
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Controller for handling Portlet view mode
 *
 * @author Richard Good
 */
@Controller
@RequestMapping("VIEW")
public class BlackboardVCPortletViewController
{
	private static final Logger logger = LoggerFactory.getLogger(BlackboardVCPortletViewController.class);

	private ConferenceUserService conferenceUserService;
	private ConferenceUserDao conferenceUserDao;
	private SessionDao sessionDao;
	
//	private SessionService sessionService;
//	private RecordingService recordingService;
//	private UserService userService;
	
	
	
	@Autowired
	public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

//    @Autowired
//	public void setSessionService(SessionService sessionService)
//	{
//		this.sessionService = sessionService;
//	}

//	@Autowired
//	public void setRecordingService(RecordingService recordingService)
//	{
//		this.recordingService = recordingService;
//	}

	@Autowired
    public void setConferenceUserService(ConferenceUserService conferenceUserService) {
        this.conferenceUserService = conferenceUserService;
    }

	@Autowired
    public void setConferenceUserDao(ConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }

//	@Autowired
//	public void setUserService(UserService userService)
//	{
//		this.userService = userService;
//	}

	@RenderMapping
	public String view(PortletRequest request, ModelMap model)
	{
		final ConferenceUser conferenceUser = this.conferenceUserService.getCurrentConferenceUser();
		
		//TODO need logic like this to find "alias" users, perhaps we deal with this more at the data model level by merging users together
		//this.conferenceUserDao.findAllMatchingUsers(blackboardUser.getEmail(), blackboardUser.getAttributes());
		
		final Set<Session> sessions = new HashSet<Session>();
		
		final Set<Session> ownedSessionsForUser = this.conferenceUserDao.getOwnedSessionsForUser(conferenceUser);
        sessions.addAll(ownedSessionsForUser);
            
        final Set<Session> chairedSessionsForUser = this.conferenceUserDao.getChairedSessionsForUser(conferenceUser);
        sessions.addAll(chairedSessionsForUser);
        
        final Set<Session> nonChairedSessionsForUser = this.conferenceUserDao.getNonChairedSessionsForUser(conferenceUser);
        sessions.addAll(nonChairedSessionsForUser);

		model.addAttribute("sessions", sessions);
		
		//TODO get & add recordings, presentations & multimediate files
		return "BlackboardVCPortlet_view";
	}
	
	/**
	 * Launch page for a specific session
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RenderMapping(params = "action=viewSession")
	public String viewSession(PortletRequest request, @RequestParam long sessionId, ModelMap model)
	{
	    logger.debug("viewSession called");

		logger.debug("sessionId:" + sessionId);

//		try
//		{
			logger.debug("calling sessionService.getSession");
			Session session = this.sessionDao.getSession(sessionId);
			model.addAttribute("session", session);

			logger.debug("done call");
			if (session == null)
			{
				logger.error("session is null!");
			}

			final ConferenceUser blackboardUser = this.conferenceUserService.getCurrentConferenceUser();
            final Set<ConferenceUser> sessionChairs = this.sessionDao.getSessionChairs(session);
            
	        if (canEdit(session, blackboardUser)) {
	            model.addAttribute("currUserCanEdit", true);
	        }
	        else
			{
			    model.addAttribute("currUserCanEdit", false);
			}
	        
	        
			if (session.getEndTime().isAfterNow())
			{
				if (sessionChairs.contains(blackboardUser))
				{
					// Removing the username parameter will make collaborate prompt for the person's name
					model.addAttribute("guestUrl", session.getGuestUrl().replaceFirst("&username=Guest", ""));
				}

				logger.debug("Session is still open, we can show the launch url");
				final Set<ConferenceUser> sessionNonChairs = this.sessionDao.getSessionNonChairs(session);
				
				// If the user is specified in chair or non chair list then get the URL
				if (sessionChairs.contains(blackboardUser) || sessionNonChairs.contains(blackboardUser))
				{
				    //TODO need to lookup the UserSessionUrl
				    
					model.addAttribute("showLaunchSession", true);
					logger.debug("User is in the chair/non-chair list");
					logger.debug("gotten user sessionUrl");
					model.addAttribute("launchSessionUrl", "TODO");
				}
			} else
			{
				logger.debug("Session is closed");
				model.addAttribute("showLaunchSession", false);
			}


			model.addAttribute("session", session);
			if (sessionChairs.contains(blackboardUser))
			{
				model.addAttribute("showCSVDownload", true);
			}
			
			//TODO use spring error handling
//		catch (Exception e)
//		{
//			logger.error("error caught:", e);
//			model.addAttribute("errorMessage", "A problem occurred retrieving the session details. If this problem re-occurs please contact support.");
//		}
		return "BlackboardVCPortlet_viewSession";

	}

    private boolean canEdit(Session session, final ConferenceUser blackboardUser) {
        if (blackboardUser.equals(session.getCreator())) {
            return true;
        }
            
        final Set<ConferenceUser> sessionChairs = this.sessionDao.getSessionChairs(session);
        return sessionChairs.contains(blackboardUser);
    }

    /**
	 * CSV Download function
	 *
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@ResourceMapping(value = "csvDownload")
	public void csvDownload(ResourceRequest request, @RequestParam long sessionId, ResourceResponse response) throws IOException
	{
		logger.debug("csvDownload called");
		logger.debug("sessionId:" + sessionId);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/csv");
		response.setProperty("Content-Disposition", "inline; filename=participantList_" + sessionId + ".csv");

		final ConferenceUser blackboardUser = this.conferenceUserService.getCurrentConferenceUser();

		PrintWriter stringWriter = null;
		try
		{
			stringWriter = response.getWriter();
			//ByteArrayOutputStream outputStream = new ByteArrayOutputStream(response.getPortletOutputStream());
			stringWriter.println("UID,Display Name,Email address,Participant type");
			logger.debug("calling sessionService.getSession");
			
			final Session session = this.sessionDao.getSession(sessionId);
			
			logger.debug("done call");
			if (session == null) {
				logger.error("session is null!");
				return;
			}
			
			final Set<ConferenceUser> sessionChairs = this.sessionDao.getSessionChairs(session);
            if (!sessionChairs.contains(blackboardUser)) {
				logger.warn("User not authorised to see csv");
				return;
			}

            
			logger.debug("Adding chair list into moderators");
			for (final ConferenceUser user : sessionChairs) {
				stringWriter.println(user.getEmail() + "," + user.getDisplayName() + "," + user.getEmail() + ",Moderator");
			}
			logger.debug("added moderators to CSV output");


			final Set<ConferenceUser> sessionNonChairs = this.sessionDao.getSessionNonChairs(session);
			logger.debug("Adding nonchair list to participants");
            for (final ConferenceUser user : sessionNonChairs) {
                if (user.getAttributes().isEmpty()) {
                    stringWriter.println(user.getEmail() + "," + user.getDisplayName() + "," + user.getEmail() + ",External Participant");
                }
                else {
                    stringWriter.println(user.getEmail() + "," + user.getDisplayName() + "," + user.getEmail() + ",Internal Participant");
                }
            }
		}
		finally {
		    stringWriter.close();
		}
		
		//TODO use spring error handling
//		catch (Exception e)
//		{
//			logger.error("Exception caught building model for CSV download", e);
//		}
	}
}
