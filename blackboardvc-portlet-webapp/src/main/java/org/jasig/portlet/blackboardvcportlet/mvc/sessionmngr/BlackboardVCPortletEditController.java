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

import freemarker.template.utility.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.data.*;
import org.jasig.portlet.blackboardvcportlet.service.*;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import javax.portlet.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller class for Portlet EDIT related actions and render
 *
 * @author Richard Good
 */
@Controller
@RequestMapping("EDIT")
public class BlackboardVCPortletEditController
{
	private static final Logger logger = LoggerFactory.getLogger(BlackboardVCPortletEditController.class);

	@Autowired
	RecordingService recordingService;

	@Autowired
	ServerConfigurationService serverConfigurationService;

	@Autowired
	SessionService sessionService;

	@Autowired
	UserService userService;

	@Autowired
	AuthorisationService authService;

	/**
	 * Standard edit render
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @return
	 * @throws Exception
	 */
	@RenderMapping
	public ModelAndView renderEditView(RenderRequest request, RenderResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("renderEditView called");
		logger.debug("session_id:" + request.getParameter("session_id"));
		logger.debug("sessionId" + request.getParameter("sessionId"));
		final PortletPreferences prefs = request.getPreferences();
		ModelAndView modelAndView = new ModelAndView("BlackboardVCPortlet_edit");

		if (mView.isEmpty())
		{
			logger.debug("mView is empty");
			ServerConfiguration serverConfiguration = serverConfigurationService.getServerConfiguration(prefs);
			Session session = new Session();
			logger.debug("session id:" + session.getSessionId());

			if (authService.isAdminAccess(request) || authService.isFullAccess(request))
			{
				logger.debug("set recording to manual");
				session.setRecordingModeType(1);
			} else
			{
				logger.debug("disable recording");
				session.setRecordingModeType(3);
			}

			session.setBoundaryTime(serverConfiguration.getBoundaryTime());
			session.setMaxCameras(serverConfiguration.getMaxAvailableCameras());
			session.setMaxTalkers(serverConfiguration.getMaxAvailableTalkers());
			session.setAllowInSessionInvites(true);
			session.setPermissionsOn(true);

			if (serverConfiguration.getMayUseSecureSignOn() == 'Y')
			{
				session.setSecureSignOn(true);
			} else
			{
				session.setSecureSignOn(false);
			}
			session.setReserveSeats(serverConfiguration.getMustReserveSeats());
			if (serverConfiguration.getRaiseHandOnEnter() == 'Y')
			{
				session.setRaiseHandOnEnter(true);
			} else
			{
				session.setRaiseHandOnEnter(false);
			}

			Date dateNow = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateNow);
			int unroundedMinutes = calendar.get(Calendar.MINUTE);
			int mod = unroundedMinutes % 15;
			int time = mod == 0 ? 15 : (15 - mod);

			calendar.add(Calendar.MINUTE, time);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			session.setStartTime(calendar.getTime());

			unroundedMinutes = calendar.get(Calendar.MINUTE);
			mod = unroundedMinutes % 15;
			time = mod == 0 ? 15 : (15 - mod);

			calendar.add(Calendar.MINUTE, time);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			session.setEndTime(calendar.getTime());
			modelAndView.addObject("session", session);
		} else
		{
			logger.debug("mView already exists");
			for (Map.Entry pairs : mView.getModelMap().entrySet())
			{
				if (pairs.getKey().equals("errorMessage"))
				{
					modelAndView.addObject("errorMessage", pairs.getValue());
				} else if (pairs.getKey().equals("feedbackMessage"))
				{
					modelAndView.addObject("feedbackMessage", pairs.getValue());
				} else if (pairs.getKey().equals("warningMessage"))
				{
					modelAndView.addObject("warningMessage", pairs.getValue());
				} else if (pairs.getKey().equals("session"))
				{
					Session session = (Session) pairs.getValue();
					modelAndView.addObject("session", session);
				} else if (pairs.getKey().equals("extParticipants"))
				{
					List<User> extParticipantList = (List<User>) pairs.getValue();
					for (int x = 0; x < extParticipantList.size(); x++)
					{
						logger.debug("list debug:(" + extParticipantList.get(x).getDisplayName() + "," + extParticipantList.get(x).getEmail() + ")");
					}
					modelAndView.addObject("extParticipants", extParticipantList);

				} else if (pairs.getKey().equals("moderators"))
				{
					List<User> moderatorList = (List<User>) pairs.getValue();
					for (int x = 0; x < moderatorList.size(); x++)
					{
						logger.debug("list debug:(" + moderatorList.get(x).getUid() + "," + moderatorList.get(x).getDisplayName() + "," + moderatorList.get(x).getEmail() + ")");
					}
					modelAndView.addObject("moderators", moderatorList);
				} else if (pairs.getKey().equals("intParticipants"))
				{
					List<User> intParticipantList = (List<User>) pairs.getValue();
					for (int x = 0; x < intParticipantList.size(); x++)
					{
						logger.debug("list debug:(" + intParticipantList.get(x).getUid() + "," + intParticipantList.get(x).getDisplayName() + "," + intParticipantList.get(x).getEmail() + ")");
					}
					modelAndView.addObject("intParticipants", intParticipantList);
				} else if (pairs.getKey().equals("presentation"))
				{
					SessionPresentation presentation = (SessionPresentation) pairs.getValue();
					modelAndView.addObject("presentation", presentation);
				} else if (pairs.getKey().equals("multimedia"))
				{
					List<SessionMultimedia> sessionMultimedia = (List<SessionMultimedia>) pairs.getValue();
					modelAndView.addObject("multimedia", sessionMultimedia);
				}
				logger.debug("map debug:" + pairs.getKey() + " = " + pairs.getValue());
			}
		}

		Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
		String eduPersonAffiliation = userInfo.get("eduPersonAffiliation");
		logger.debug("gotten affiliation:" + eduPersonAffiliation);
		if (authService.isAdminAccess(request) || authService.isFullAccess(request))
		{
			logger.debug("full access set for edit mode");
			modelAndView.addObject("fullAccess", "true");
		}

		if (modelAndView.getModelMap().get("moderators") == null)
		{
			User thisUser = new User();
			thisUser.setEmail(userInfo.get("mail"));
			thisUser.setUid(userInfo.get("uid"));
			thisUser.setDisplayName(userInfo.get("displayName"));

			List<User> moderators = new ArrayList<User>();
			moderators.add(thisUser);
			modelAndView.addObject("moderators", moderators);
			logger.debug("added initial moderator:" + moderators.size());
		}

		logger.debug("returning model");
		return modelAndView;
	}

	/**
	 * Edit an existing session
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @return
	 */
	@RenderMapping(params = "action=editSession")
	public ModelAndView editSession(RenderRequest request, RenderResponse response, ModelAndView mView)
	{
		logger.debug("editSession called");
		ModelAndView modelAndView = new ModelAndView("BlackboardVCPortlet_edit");
		String sessionId = request.getParameter("sessionId");
		logger.debug("sessionId:" + sessionId);


		if (authService.isAdminAccess(request) || authService.isFullAccess(request))
		{
			logger.debug("full access set for edit mode");
			modelAndView.addObject("fullAccess", "true");
		}

		try
		{
			logger.debug("calling sessionService.getSession");
			Session session = sessionService.getSession(Long.valueOf(sessionId));
			logger.debug("done call");
			if (session == null)
			{
				logger.error("session is null!");
			}
			modelAndView.addObject("session", session);
			User user;
			if (session.getChairList() != null && !session.getChairList().equals(""))
			{
				logger.debug("Adding chair list into moderators");
				String[] chairList = StringUtil.split(session.getChairList(), ',');
				List<User> moderatorList = new ArrayList<User>();
				for (int i = 0; i < chairList.length; i++)
				{
					user = userService.getUserDetails(chairList[i]);
					if (user != null)
					{
						moderatorList.add(user);
					} else
					{
						user = new User();
						user.setUid(chairList[i]);
						user.setDisplayName("Unknown user");
						moderatorList.add(user);
					}
				}
				modelAndView.addObject("moderators", moderatorList);
			}

			if (session.getNonChairList() != null && !session.getNonChairList().equals(""))
			{
				logger.debug("Adding nonchair list to participants");
				String[] nonChairList = StringUtil.split(session.getNonChairList(), ',');
				List<User> intParticipantList = new ArrayList<User>();
				List<User> extParticipantList = new ArrayList<User>();

				for (int i = 0; i < nonChairList.length; i++)
				{
					user = userService.getUserDetails(nonChairList[i]);
					if (user != null)
					{
						intParticipantList.add(user);
					} else
					{
						user = sessionService.getExtParticipant(session.getSessionId(), nonChairList[i]);
						if (user == null)
						{
							user = new User();
							user.setEmail(nonChairList[i]);
						}

						extParticipantList.add(user);
					}
				}

				modelAndView.addObject("intParticipants", intParticipantList);
				modelAndView.addObject("extParticipants", extParticipantList);
			}

			SessionPresentation sessionPresentation = sessionService.getSessionPresentation(Long.toString(session.getSessionId()));
			if (sessionPresentation != null)
			{
				modelAndView.addObject("presentation", sessionPresentation);
			}

			List<SessionMultimedia> sessionMultimedia = sessionService.getSessionMultimedia(session.getSessionId());
			if (sessionMultimedia != null)
			{
				modelAndView.addObject("multimedia", sessionMultimedia);
			}

		}
		catch (Exception e)
		{
			logger.error("Error retrieving session", e);
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.sessionretrievalproblem");
			modelAndView.addObject("errorMessage", errorMessage);
		}
		return modelAndView;
	}

	/**
	 * Delete one or more recordings
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=deleteRecordings")
	public void deleteRecordings(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("deleteRecordings called");
		final PortletPreferences prefs = request.getPreferences();
		String[] recordingIds = request.getParameterValues("deleteRecording");
		if (recordingIds != null)
		{
			for (int i = 0; i < recordingIds.length; i++)
			{
				logger.debug("recording to delete:" + recordingIds[i]);
				recordingService.deleteRecording(prefs, Long.valueOf(recordingIds[i]));
			}
			response.setRenderParameter("feedbackMessage", "feedbackmessage.recordingsdeleted");
		} else
		{
			response.setRenderParameter("warningMessage", "feedbackmessage.norecordingsselected");
		}

		response.setPortletMode(PortletMode.VIEW);
	}

	/**
	 * Delete one or more sessions
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=deleteSessions")
	public void deleteSessions(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		final PortletPreferences prefs = request.getPreferences();
		String[] sessionIds = request.getParameterValues("deleteSession");
		if (sessionIds != null)
		{
			for (int i = 0; i < sessionIds.length; i++)
			{
				logger.debug("session to delete:" + sessionIds[i]);
				try
				{
					sessionService.deleteSession(Long.valueOf(sessionIds[i]));
				}
				catch (Exception e)
				{
					List<String> errorMessage = new ArrayList<String>();
					errorMessage.add("error.deletesessionerror");
					mView.addObject("errorMessage", errorMessage);
				}
			}
			response.setRenderParameter("feedbackMessage", "feedbackmessage.sessionsdeleted");
		} else
		{
			response.setRenderParameter("warningMessage", "feedbackmessage.nosessionsselected");
		}

		response.setPortletMode(PortletMode.VIEW);

	}

	/**
	 * Upload multimedia file
	 *
	 * @param request
	 * @param response
	 * @param command
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Upload Multimedia")
	public void addMultimedia(ActionRequest request, ActionResponse response, SessionForm command, ModelAndView mView) throws Exception
	{
		logger.debug("addMultimedia called");

		String sessionId = request.getParameter("sessionId");

		logger.debug("for session:" + sessionId);

		mView = this.addSessionToModel(request, mView);

		logger.debug("added session to model");
		final PortletPreferences prefs = request.getPreferences();
		logger.debug("gotten prefs");
		Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
		String uid = userInfo.get("uid");

		logger.debug("gotten uid:" + uid);

		if (command.getMultimediaUpload() == null || command.getMultimediaUpload().isEmpty())
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.multimedianofileselected");
			mView.addObject("errorMessage", errorMessage);
		} else
		{
			logger.debug("File has been added:" + command.getMultimediaUpload().getOriginalFilename());
			try
			{
				sessionService.addSessionMultimedia(uid, Long.valueOf(sessionId), command.getMultimediaUpload());
				List<SessionMultimedia> sessionMultimedia = sessionService.getSessionMultimedia(Long.valueOf(sessionId));
				mView.addObject("multimedia", sessionMultimedia);
			}
			catch (Exception e)
			{
				List<String> errorMessage = new ArrayList<String>();
				errorMessage.add("error.multimediafileupload");
				logger.error("Exception caught", e);
				mView.addObject("errorMessage", errorMessage);
				return;
			}

		}

		mView.addObject("feedbackMessage", "feedbackmessage.multimediaadded");
	}

	/**
	 * Upload a presentation file
	 *
	 * @param request
	 * @param response
	 * @param command
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Upload Presentation")
	public void addPresentation(ActionRequest request, ActionResponse response, SessionForm command, ModelAndView mView) throws Exception
	{

		logger.debug("addPresentation called");

		String sessionId = request.getParameter("sessionId");

		logger.debug("for session:" + sessionId);

		mView = this.addSessionToModel(request, mView);

		logger.debug("added session to model");
		final PortletPreferences prefs = request.getPreferences();
		logger.debug("gotten prefs");
		Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
		String uid = userInfo.get("uid");

		logger.debug("gotten uid:" + uid);

		if (command.getPresentationUpload() == null || command.getPresentationUpload().isEmpty())
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.presentationnofileselected");
			mView.addObject("errorMessage", errorMessage);
			return;
		} else if (sessionService.getSessionPresentation(sessionId) != null)
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.presentationmustbedeleted");
			mView.addObject("errorMessage", errorMessage);
			return;
		} else
		{
			logger.debug("File has been added:" + command.getPresentationUpload().getOriginalFilename());
			try
			{
				sessionService.addSessionPresentation(uid, Long.valueOf(sessionId), command.getPresentationUpload());
				SessionPresentation sessionPresentation = sessionService.getSessionPresentation(sessionId);
				mView.addObject("presentation", sessionPresentation);
			}
			catch (Exception e)
			{
				logger.error("Exception caught", e);
				List<String> errorMessage = new ArrayList<String>();
				errorMessage.add("error.presentationupload");
				mView.addObject("errorMessage", errorMessage);
				return;
			}

		}

		mView.addObject("feedbackMessage", "feedbackmessage.presentationadded");


	}

	/**
	 * Add participants
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Add Participant(s)")
	public void addIntParticipant(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("addInternalParticipant called");
		mView = this.addSessionToModel(request, mView);
		String participantString = request.getParameter("intParticipants");
		List<String> errorMessage = new ArrayList<String>();
		if (participantString == null || participantString.equals(""))
		{
			errorMessage.add("error.noparticipantstoadd");
			mView.addObject("errorMessage", errorMessage);
		} else
		{
			logger.debug("intParticipants not null, adding");
			StringTokenizer st = new StringTokenizer(participantString, ",");

			String userToLookup;
			int numToSearch = st.countTokens();
			boolean hasErrors = false;
			User user;
			List<User> moderatorList;
			if (mView.getModelMap().get("intParticipants") != null)
			{
				moderatorList = (List<User>) mView.getModelMap().get("intParticipants");
			} else
			{
				moderatorList = new ArrayList<User>();
			}
			while (st.hasMoreTokens())
			{
				userToLookup = st.nextToken();
				user = userService.getUserDetails(userToLookup);
				if (user == null)
				{
					hasErrors = true;

					errorMessage.add("User '" + userToLookup + "' not found</li>");
				} else
				{

					moderatorList.add(user);
					logger.debug("added user to list:(" + user.getDisplayName() + "," + user.getEmail() + ")");

				}
			}
			mView.addObject("intParticipants", moderatorList);

			if (hasErrors)
			{
				if (numToSearch > errorMessage.size())
				{
					mView.addObject("warningMessage", "warningmessage.someinternalparticipantsadded");
				}
				mView.addObject("errorMessage", errorMessage);
			} else
			{
				mView.addObject("feedbackMessage", "feedbackmessage.internalparticipantsadded");
			}
		}

	}

	/**
	 * Delete moderators
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Delete Moderator(s)")
	public void deleteModerators(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("deleteModerators called");
		mView = this.addSessionToModel(request, mView);
		String[] moderatorsToDelete = request.getParameterValues("deleteModerator");
		if (moderatorsToDelete == null || moderatorsToDelete.length == 0)
		{
			List<String> errorMessage = new ArrayList();
			errorMessage.add("error.nomoderatorsselected");
			mView.addObject("errorMessage", errorMessage);
		} else
		{

			int delRow;
			List<User> moderatorList = (List<User>) mView.getModelMap().get("moderators");
			for (int i = (moderatorsToDelete.length - 1); i > -1; i--)
			{
				delRow = new Integer(moderatorsToDelete[i]);
				moderatorList.remove(delRow);
			}
			mView.addObject("moderators", moderatorList);
			mView.addObject("feedbackMessage", "feedbackmessage.moderatorsremoved");
		}

	}

	/**
	 * Remove internal participants
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Delete Internal Participant(s)")
	public void deleteInternalParticipant(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("deleteInternalParticipant called");
		mView = this.addSessionToModel(request, mView);
		String[] intParticipantsToDelete = request.getParameterValues("deleteIntParticipant");
		if (intParticipantsToDelete == null || intParticipantsToDelete.length == 0)
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.nointernalparticipantselected");
			mView.addObject("errorMessage", errorMessage);
		} else
		{

			int delRow;
			List<User> moderatorList = (List<User>) mView.getModelMap().get("intParticipants");
			for (int i = (intParticipantsToDelete.length - 1); i > -1; i--)
			{
				delRow = new Integer(intParticipantsToDelete[i]);
				moderatorList.remove(delRow);
			}
			mView.addObject("intParticipants", moderatorList);
			mView.addObject("feedbackMessage", "feedbackmessage.internalparticipantsremoved");
		}
	}

	/**
	 * Remove multimedia files
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Delete Multimedia Item(s)")
	public void deleteMultimedia(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("deleteMultimedia called");
		mView = this.addSessionToModel(request, mView);
		final PortletPreferences prefs = request.getPreferences();
		long sessionId = Long.valueOf(request.getParameter("sessionId"));
		String[] multiMediaIds = request.getParameterValues("deleteMultimediaFiles");

		if (multiMediaIds == null || multiMediaIds.length == 0)
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.multimedianofilestodelete");
			mView.addObject("errorMessage", errorMessage);
			return;
		}

		try
		{
			sessionService.deleteSessionMultimediaFiles(sessionId, multiMediaIds);
		}
		catch (Exception e)
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.multimediadelete");
			logger.error("Error caught deleting multimedia", e);
			mView.addObject("errorMessage", errorMessage);
			return;
		}

		mView.addObject("feedbackMessage", "feedbackmessage.multimediaremoved");
		List<SessionMultimedia> sessionMultimedia = sessionService.getSessionMultimedia(sessionId);

		mView.addObject("multimedia", sessionMultimedia);


	}

	/**
	 * Delete a presentation
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Delete Presentation")
	public void deletePresentation(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("deletePresentation called");
		mView = this.addSessionToModel(request, mView);
		final PortletPreferences prefs = request.getPreferences();
		long sessionId = Long.valueOf(request.getParameter("sessionId"));
		long presentationId = Long.valueOf(request.getParameter("presentationId"));
		try
		{
			sessionService.deleteSessionPresentation(sessionId, presentationId);
			mView.getModelMap().remove("presentation");
		}
		catch (Exception e)
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.presentationdelete");
			logger.error("Error caught deleting presentation", e);
			mView.addObject("errorMessage", errorMessage);
		}

		mView.addObject("feedbackMessage", "feedbackmessage.presentationremoved");

	}

	/**
	 * Delete external participants
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Delete External Participant(s)")
	public void deleteExternalParticipant(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("deleteExternalParticipant called");
		mView = this.addSessionToModel(request, mView);
		String[] extParticipantsToDelete = request.getParameterValues("deleteExtParticipant");
		if (extParticipantsToDelete == null || extParticipantsToDelete.length == 0)
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.noextparticipantstodelete");
			mView.addObject("errorMessage", errorMessage);
		} else
		{

			int delRow;
			List<User> moderatorList = (List<User>) mView.getModelMap().get("extParticipants");
			for (int i = (extParticipantsToDelete.length - 1); i > -1; i--)
			{
				delRow = new Integer(extParticipantsToDelete[i]);
				moderatorList.remove(delRow);
			}
			mView.addObject("extParticipants", moderatorList);
			mView.addObject("feedbackMessage", "feedbackmessage.externalparticipantsremoved");
		}

	}

	/**
	 * Add external participants
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Add External Participant")
	public void addExternalParticipant(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("addExternalParticipant called");
		mView = this.addSessionToModel(request, mView);
		String displayName = request.getParameter("extParticipantDisplayName");
		String email = request.getParameter("extParticipantEmail");
		if (displayName == null || displayName.equals("") || email == null || email.equals(""))
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.extparticipantdisplayandemailmustbeprovided");
			mView.addObject("errorMessage", errorMessage);
		} else
		{
			List<User> extParticipantList;
			if (mView.getModelMap().get("extParticipants") != null)
			{
				extParticipantList = (List<User>) mView.getModelMap().get("extParticipants");
			} else
			{
				extParticipantList = new ArrayList<User>();
			}

			User user = new User();
			user.setDisplayName(displayName);
			user.setEmail(email);
			extParticipantList.add(user);
			logger.debug("added user to list:(" + user.getDisplayName() + "," + user.getEmail() + ")");
			mView.addObject("extParticipants", extParticipantList);
			logger.debug("added extParticipants to model");
			mView.addObject("feedbackMessage", "feedbackmessage.externalparticipantsadded");
		}
	}

	/**
	 * Add moderators
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Add Moderator(s)")
	public void addModerator(ActionRequest request, ActionResponse response, ModelAndView mView) throws Exception
	{
		logger.debug("addModerator called");
		mView = this.addSessionToModel(request, mView);
		String moderatorString = request.getParameter("moderatorUid");
		if (moderatorString == null || moderatorString.equals(""))
		{
			List<String> errorMessage = new ArrayList<String>();
			errorMessage.add("error.nomoderatorstoadd");
			mView.addObject("errorMessage", errorMessage);
		} else
		{
			StringTokenizer st = new StringTokenizer(moderatorString, ",");
			String userToLookup;
			List<String> errorMessage = new ArrayList<String>();
			boolean hasErrors = false;
			User user;
			List<User> moderatorList;
			if (mView.getModelMap().get("moderators") != null)
			{
				moderatorList = (List<User>) mView.getModelMap().get("moderators");
			} else
			{
				moderatorList = new ArrayList<User>();
			}
			int numToSearch = st.countTokens();
			while (st.hasMoreTokens())
			{
				userToLookup = st.nextToken();
				user = userService.getUserDetails(userToLookup);
				if (user == null)
				{
					hasErrors = true;

					errorMessage.add("User '" + userToLookup + "' not found</li>");
				} else
				{

					moderatorList.add(user);
					logger.debug("added user to list:(" + user.getDisplayName() + "," + user.getEmail() + ")");

				}
			}
			mView.addObject("moderators", moderatorList);

			if (hasErrors)
			{
				if (numToSearch > errorMessage.size())
				{
					mView.addObject("warningMessage", "warningmessage.somemoderatorsadded");
				}
				mView.addObject("errorMessage", errorMessage);
			} else
			{
				mView.addObject("feedbackMessage", "feedbackmessage.moderatorsadded");
			}

		}

	}

	/**
	 * Save a session
	 *
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Save Session")
	public void saveSession(ActionRequest request, ActionResponse response, ModelAndView modelAndView) throws Exception
	{

		final PortletPreferences prefs = request.getPreferences();

		logger.debug("saveSession called");
		boolean noErrors = true;
		Session session = new Session();
		boolean newSession = false;

		if (!request.getParameter("sessionId").equals("") && !request.getParameter("sessionId").equals("0"))
		{
			logger.debug("pre-existing session, getting session id");
			String sessionId = request.getParameter("sessionId");
			session.setSessionId(Long.valueOf(sessionId));
		} else
		{
			newSession = true;
		}

		//StringBuilder errorMessage = new StringBuilder();
		List<String> errorMessage = new ArrayList();

		if (modelAndView.getModelMap().get("session") != null)
		{
			logger.debug("session is in the model");
		}

		session.setAccessType(2);
		if (request.getParameter("sessionName").equals(""))
		{
			noErrors = false;
			errorMessage.add("error.sessionnamemissing");
		} else
		{
			session.setSessionName(request.getParameter("sessionName"));
		}

		Date startDate = null;
		try
		{
			startDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).parse(request.getParameter("startdate") + " " + request.getParameter("startHour") + ":" + request.getParameter("startMinute"));
			session.setStartTime(startDate);

		}
		catch (Exception e)
		{
			noErrors = false;
			errorMessage.add("error.startdateinvalid");
		}

		Date endDate = null;
		try
		{
			endDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).parse(request.getParameter("enddate") + " " + request.getParameter("endHour") + ":" + request.getParameter("endMinute"));
			session.setEndTime(endDate);
		}
		catch (Exception e)
		{
			noErrors = false;
			errorMessage.add("error.enddateinvalid");
		}

		if (startDate != null && endDate != null)
		{
			if (endDate.before(startDate))
			{
				noErrors = false;
				errorMessage.add("error.startdatebeforeenddate");
			}

			Date dateNow = new Date();

			if (startDate.before(dateNow))
			{
				noErrors = false;
				errorMessage.add("error.startdatebeforenow");
			}

			DateTime startDateTime = new DateTime(startDate);
			DateTime endDateTime = new DateTime(endDate);

			Period period = new Period(startDateTime, endDateTime);
			int years = period.getYears();

			if (years > 0)
			{
				noErrors = false;
				errorMessage.add(("error.dateyearapart"));
			}


		} else
		{
			logger.debug("dates were null");
		}

		logger.debug("Finished validation.");

		try
		{

			Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);

			logger.debug("Gotten user map");

			//Session session = new Session();
			session.setAccessType(2);

			logger.debug("Set access type");

			if (request.getParameter("allowInSessionInvites") != null && !request.getParameter("allowInSessionInvites").equals("false"))
			{
				session.setAllowInSessionInvites(true);
			} else
			{
				session.setAllowInSessionInvites(false);
			}

			session.setCreatorOrgUnit(request.getParameter("creatorOrgUnit"));

			logger.debug("Set allowInSessionInvites");

			session.setBoundaryTime(new Integer(request.getParameter("boundaryTime")));
			//session.setChairList(null);
			//session.setChairNotes(null);
			session.setCreatorId(userInfo.get("uid"));
			//session.setGroupingList(null);

			session.setCreatorOrgUnit(userInfo.get("eduPersonOrgUnitDN"));

			logger.debug("Set boundaryTime");
			if (request.getParameter("hideParticipantNames") != null && !request.getParameter("hideParticipantNames").equals("false"))
			{
				session.setHideParticipantNames(true);
			} else
			{
				session.setHideParticipantNames(false);
			}

			logger.debug("Set hideParticipantNames");
			session.setLastUpdated(new Date());

			session.setMaxCameras(new Integer(request.getParameter("maxCameras")));

			logger.debug("Set max cameras");
			session.setMaxTalkers(new Integer(request.getParameter("maxTalkers")));

			logger.debug("Set max talkers");


			if (request.getParameter("mustBeSupervised") != null && !request.getParameter("mustBeSupervised").equals("false"))
			{
				session.setMustBeSupervised(true);
			} else
			{
				session.setMustBeSupervised(false);
			}

			logger.debug("Set must be supervised");

			session.setNonChairList(null);
			session.setNonChairNotes(null);
			session.setOpenChair(false);

			logger.debug("set chair settings");
			if (request.getParameter("permissionsOn") != null && !request.getParameter("permissionsOn").equals("false"))
			{
				session.setPermissionsOn(true);
			} else
			{
				session.setPermissionsOn(false);
			}

			logger.debug("Set permissionsOn");
			if (request.getParameter("raiseHandOnEnter") != null && !request.getParameter("raiseHandOnEnter").equals("false"))
			{
				session.setRaiseHandOnEnter(true);
			} else
			{
				session.setRaiseHandOnEnter(false);
			}

			logger.debug("Set raiseHandOnEnter");

			if (request.getParameter("recordingModeType").equals("1"))
			{
				session.setRecordingModeType(1);
			} else if (request.getParameter("recordingModeType").equals("2"))
			{
				session.setRecordingModeType(2);
			} else
			{
				session.setRecordingModeType(3);
			}

			logger.debug("Set recordingModeType");
			session.setRecordings(false);

			session.setSecureSignOn(false);

			List<User> moderatorList = new ArrayList<User>();

			String[] moderatorUids = request.getParameterValues("moderatorUids");
			String[] moderatorEmails = request.getParameterValues("moderatorEmails");
			String[] moderatorDisplayNames = request.getParameterValues("moderatorDisplayNames");
			if (moderatorUids != null)
			{
				User user;
				for (int i = 0; i < moderatorUids.length; i++)
				{
					user = new User();
					user.setUid(moderatorUids[i]);
					user.setEmail(moderatorEmails[i]);
					user.setDisplayName(moderatorDisplayNames[i]);
					logger.debug("added user:(" + user.getUid() + "," + user.getDisplayName() + "," + user.getEmail() + ")");
					moderatorList.add(user);
				}
				session.setChairList(StringUtils.join(moderatorUids, ","));

			}

			modelAndView.addObject("moderators", moderatorList);

			List<User> extParticipantList = new ArrayList<User>();

			String[] extParticipantEmails = request.getParameterValues("extParticipantEmails");
			String[] extParticipantDisplayNames = request.getParameterValues("extParticipantDisplayNames");
			String nonChairList = "";
			if (extParticipantEmails != null)
			{
				User user;
				for (int i = 0; i < extParticipantEmails.length; i++)
				{
					user = new User();
					user.setEmail(extParticipantEmails[i]);
					user.setDisplayName(extParticipantDisplayNames[i]);
					extParticipantList.add(user);
					logger.debug("added user:(" + user.getDisplayName() + "," + user.getEmail() + ")");
				}
				nonChairList = StringUtils.join(extParticipantEmails, ",");
			}

			modelAndView.addObject("extParticipants", extParticipantList);

			List<User> intParticipantList = new ArrayList<User>();

			String[] intParticipantUids = request.getParameterValues("intParticipantUids");
			String[] intParticipantEmails = request.getParameterValues("intParticipantEmails");
			String[] intParticipantDisplayNames = request.getParameterValues("intParticipantDisplayNames");
			if (intParticipantUids != null)
			{
				User user;
				for (int i = 0; i < intParticipantUids.length; i++)
				{
					user = new User();
					user.setUid(intParticipantUids[i]);
					user.setEmail(intParticipantEmails[i]);
					user.setDisplayName(intParticipantDisplayNames[i]);
					logger.debug("added user:(" + user.getUid() + "," + user.getDisplayName() + "," + user.getEmail() + ")");
					intParticipantList.add(user);
				}
				if (!nonChairList.equals(""))
				{
					nonChairList = nonChairList + "," + StringUtils.join(intParticipantUids, ",");
				} else
				{
					nonChairList = StringUtils.join(intParticipantUids, ",");
				}
			}

			session.setNonChairList(nonChairList);
			modelAndView.addObject("intParticipants", intParticipantList);

			if (!noErrors)
			{
				logger.debug("Validation errors are going to be passed back to user." + errorMessage.toString());
				response.setPortletMode(PortletMode.EDIT);
				modelAndView.addObject("errorMessage", errorMessage);
				modelAndView.addObject("session", session);
			} else
			{
				// Validation passed, send the session to collaborate
				try
				{
					sessionService.createEditSession(session, prefs, extParticipantList);
				}
				catch (Exception e)
				{
					logger.error("Exception caught creating service", e);
					errorMessage.add("An error occurred while creating your session:" + e.toString() + ". If this error re-occurs please contact support.");
					response.setPortletMode(PortletMode.EDIT);
					modelAndView.addObject("errorMessage", errorMessage);
					modelAndView.addObject("session", session);
					noErrors = false;
				}

				if (noErrors)
				{

					User creatorUser = new User();
					creatorUser.setEmail(userInfo.get("mail"));
					creatorUser.setUid(userInfo.get("uid"));
					creatorUser.setDisplayName(userInfo.get("displayName"));

					try
					{
						if (newSession)
						{
							String launchUrl;
							launchUrl = request.getScheme() + "://" + request.getServerName();
							if (request.getScheme().equals("http") && request.getServerPort() != 80)
							{
								launchUrl += ":" + request.getServerPort();
							}
							launchUrl += "/uPortal/render.userLayoutRootNode.uP?uP_fname=";
							launchUrl += prefs.getValue("fname", "blackboardvc-portlet");

							logger.debug("New session, notifying participants");
							sessionService.notifyModerators(creatorUser, session, moderatorList, launchUrl);
							sessionService.notifyIntParticipants(creatorUser, session, intParticipantList, launchUrl);
							sessionService.notifyExtParticipants(creatorUser, session, extParticipantList);
						}

						response.setRenderParameter("feedbackMessage", "feedbackmessage.sessionsaved");
						response.setPortletMode(PortletMode.VIEW);

					}
					catch (Exception e)
					{
						logger.error("Exception caught mailing attendess", e);
						errorMessage.add("error.notificationproblem");
						modelAndView.addObject("errorMessage", errorMessage);
					}

				}

			}

		}
		catch (Exception e)
		{
			logger.error("Exception caught creating session", e);
			modelAndView.addObject("session", session);
			response.setPortletMode(PortletMode.EDIT);
			modelAndView.addObject("errorMessage", "error" + e.toString());
		}


	}

	/**
	 * Helper class for adding the session to the ModelAndView
	 *
	 * @param request
	 * @param modelAndView
	 * @return
	 */
	private ModelAndView addSessionToModel(ActionRequest request, ModelAndView modelAndView)
	{
		logger.debug("addSessionToModel called");

		Session session = new Session();

		if (!request.getParameter("sessionId").equals(""))
		{
			logger.debug("pre-existing session, getting session id");
			String sessionId = request.getParameter("sessionId");
			session.setSessionId(Long.valueOf(sessionId));
		}

		if (modelAndView.getModelMap().get("session") != null)
		{
			logger.debug("session is in the model");
		}

		session.setAccessType(2);

		session.setSessionName(request.getParameter("sessionName"));

		Date startDate = null;
		try
		{
			startDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).parse(request.getParameter("startdate") + " " + request.getParameter("startHour") + ":" + request.getParameter("startMinute"));
			session.setStartTime(startDate);

		}
		catch (Exception e)
		{
		}


		Date endDate = null;
		try
		{
			endDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH).parse(request.getParameter("enddate") + " " + request.getParameter("endHour") + ":" + request.getParameter("endMinute"));
			session.setEndTime(endDate);
		}
		catch (Exception e)
		{
		}

		Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);

		logger.debug("Gotten user map");

		//Session session = new Session();
		session.setAccessType(2);

		logger.debug("Set access type");

		if (request.getParameter("allowInSessionInvites") != null)
		{
			session.setAllowInSessionInvites(true);
		} else
		{
			session.setAllowInSessionInvites(false);
		}

		session.setCreatorOrgUnit(request.getParameter("creatorOrgUnit"));

		logger.debug("Set allowInSessionInvites");

		session.setBoundaryTime(new Integer(request.getParameter("boundaryTime")));

		session.setCreatorId(userInfo.get("uid"));


		session.setCreatorOrgUnit(userInfo.get("eduPersonOrgUnitDN"));

		logger.debug("Set boundaryTime");
		if (request.getParameter("hideParticipantNames") != null)
		{
			session.setHideParticipantNames(true);
		} else
		{
			session.setHideParticipantNames(false);
		}

		logger.debug("Set hideParticipantNames");


		session.setMaxCameras(new Integer(request.getParameter("maxCameras")));

		logger.debug("Set max cameras");
		session.setMaxTalkers(new Integer(request.getParameter("maxTalkers")));

		logger.debug("Set max talkers");


		if (request.getParameter("mustBeSupervised") != null)
		{
			session.setMustBeSupervised(true);
		} else
		{
			session.setMustBeSupervised(false);
		}

		logger.debug("Set must be supervised");

		session.setNonChairList(null);
		session.setNonChairNotes(null);
		session.setOpenChair(false);

		logger.debug("set chair settings");
		if (request.getParameter("permissionsOn") != null)
		{
			session.setPermissionsOn(true);
		} else
		{
			session.setPermissionsOn(false);
		}

		logger.debug("Set permissionsOn");
		if (request.getParameter("raiseHandOnEnter") != null && !request.getParameter("raiseHandOnEnter").equals("false"))
		{
			session.setRaiseHandOnEnter(true);
		} else
		{
			session.setRaiseHandOnEnter(false);
		}

		logger.debug("Set raiseHandOnEnter");

		if (request.getParameter("recordingModeType").equals("1"))
		{
			session.setRecordingModeType(1);
		} else if (request.getParameter("recordingModeType").equals("2"))
		{
			session.setRecordingModeType(2);
		} else
		{
			session.setRecordingModeType(3);
		}

		logger.debug("Set recordingModeType");
		session.setRecordings(false);

		session.setSecureSignOn(false);

		List<User> moderatorList = new ArrayList<User>();

		String[] moderatorUids = request.getParameterValues("moderatorUids");
		String[] moderatorEmails = request.getParameterValues("moderatorEmails");
		String[] moderatorDisplayNames = request.getParameterValues("moderatorDisplayNames");
		if (moderatorUids != null)
		{
			User user;
			for (int i = 0; i < moderatorUids.length; i++)
			{
				user = new User();
				user.setUid(moderatorUids[i]);
				user.setEmail(moderatorEmails[i]);
				user.setDisplayName(moderatorDisplayNames[i]);
				logger.debug("added user:(" + user.getUid() + "," + user.getDisplayName() + "," + user.getEmail() + ")");
				moderatorList.add(user);
			}
		}

		modelAndView.addObject("moderators", moderatorList);

		List<User> extParticipantList = new ArrayList<User>();

		String[] extParticipantEmails = request.getParameterValues("extParticipantEmails");
		String[] extParticipantDisplayNames = request.getParameterValues("extParticipantDisplayNames");
		if (extParticipantEmails != null)
		{
			User user;
			for (int i = 0; i < extParticipantEmails.length; i++)
			{
				user = new User();
				user.setEmail(extParticipantEmails[i]);
				user.setDisplayName(extParticipantDisplayNames[i]);
				logger.debug("added user:(" + user.getDisplayName() + "," + user.getEmail() + ")");
				extParticipantList.add(user);
			}
		}

		modelAndView.addObject("extParticipants", extParticipantList);
		List<User> intParticipantList = new ArrayList<User>();

		String[] intParticipantUids = request.getParameterValues("intParticipantUids");
		String[] intParticipantEmails = request.getParameterValues("intParticipantEmails");
		String[] intParticipantDisplayNames = request.getParameterValues("intParticipantDisplayNames");
		if (intParticipantUids != null)
		{
			User user;
			for (int i = 0; i < intParticipantUids.length; i++)
			{
				user = new User();
				user.setUid(intParticipantUids[i]);
				user.setEmail(intParticipantEmails[i]);
				user.setDisplayName(intParticipantDisplayNames[i]);
				logger.debug("added user:(" + user.getUid() + "," + user.getDisplayName() + "," + user.getEmail() + ")");
				intParticipantList.add(user);
			}
		}

		modelAndView.addObject("intParticipants", intParticipantList);

		SessionPresentation sessionPresentation = sessionService.getSessionPresentation(Long.toString(session.getSessionId()));
		if (sessionPresentation != null)
		{
			modelAndView.addObject("presentation", sessionPresentation);
		}

		List<SessionMultimedia> sessionMultimedia = sessionService.getSessionMultimedia(session.getSessionId());
		if (sessionMultimedia != null)
		{
			modelAndView.addObject("multimedia", sessionMultimedia);
		}

		modelAndView.addObject("session", session);

		return modelAndView;

	}

	/**
	 * Edit an existing recording
	 *
	 * @param request
	 * @param response
	 * @param mView
	 * @return
	 */
	@RenderMapping(params = "action=editRecording")
	public ModelAndView editRecording(RenderRequest request, RenderResponse response, ModelAndView mView)
	{
		logger.debug("editRecording called");
		ModelAndView modelAndView = new ModelAndView("BlackboardVCPortlet_editRecording");

		if (request.getParameter("errorMessage") != null)
		{
			modelAndView.addObject("errorMessage", request.getParameter("errorMessage"));
		}

		String recordingId = request.getParameter("recordingId");
		RecordingShort recordingShort = recordingService.getRecording(Long.valueOf(recordingId));
		modelAndView.addObject("recording", recordingShort);
		return modelAndView;
	}

	/**
	 * Save a recording
	 *
	 * @param request
	 * @param response
	 * @param modelAndView
	 * @throws Exception
	 */
	@ActionMapping(params = "action=Save Recording")
	public void saveRecording(ActionRequest request, ActionResponse response, ModelAndView modelAndView) throws Exception
	{

		String roomName = request.getParameter("roomName");
		String recordingId = request.getParameter("recordingId");
		if (roomName == null || roomName.equals(""))
		{
			String errorMessage = "error.roomnameisnull";
			response.setRenderParameter("errorMessage", errorMessage);
			response.setRenderParameter("action", "editRecording");
			response.setRenderParameter("recordingId", recordingId);
		} else
		{
			RecordingShort recordingShort = recordingService.getRecording(Long.valueOf(recordingId));
			recordingShort.setRoomName(roomName);
			recordingService.saveRecordingShort(recordingShort);
			response.setRenderParameter("feedbackMessage", "feedbackmessage.recordingsaved");
			response.setPortletMode(PortletMode.VIEW);
		}
	}
}
