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

import com.google.common.collect.Ordering;
import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.data.*;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.AddModeratorForm;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.AddParticipantForm;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.DeleteModeratorsForm;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.DeleteParticipantsForm;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.jasig.portlet.blackboardvcportlet.service.ServerConfigurationService;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Set;

/**
 * Controller class for Portlet EDIT related actions and render
 *
 * @author Richard Good
 */
@Controller
@RequestMapping("EDIT")
public class SessionCreateEditController
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ConferenceUserService conferenceUserService;
	private ServerConfigurationService serverConfigurationService;
	private SessionService sessionService;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ViewSessionListController viewController;

	@Value("${maxuploadsize}")
	private Integer maxFileUploadSize;

	@Value("${presentationFileTypes}")
	private String presentationFileTypes;

	@Value("${multimediaFileTypes}")
	private String multimediaFileTypes;

	@Autowired
	public void setConferenceUserService(ConferenceUserService conferenceUserService) {
        this.conferenceUserService = conferenceUserService;
    }

	@Autowired
	public void setServerConfigurationService(ServerConfigurationService serverConfigurationService)
	{
		this.serverConfigurationService = serverConfigurationService;
	}

	@Autowired
	public void setSessionService(SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@InitBinder
    public void initBinder(WebDataBinder binder) {
	    final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("MM/dd/yyyy").toFormatter();
        binder.registerCustomEditor(DateMidnight.class, new CustomDateMidnightEditor(formatter, false));
    }
	
	@ModelAttribute("recordingModes")
	public RecordingMode[] getRecordingModes() {
	    return RecordingMode.values();
	}

	@RenderMapping(params="action=createAndEditSession")
	public String createAndEditSession(RenderRequest request, ModelMap model, @RequestParam("name") String sessionName) throws PortletModeException {
	    final ServerConfiguration serverConfiguration = this.serverConfigurationService.getServerConfiguration();
        model.put("serverConfiguration", serverConfiguration);

		if (!model.containsKey("sessionForm"))
		{
			SessionForm sessionForm = new SessionForm(serverConfiguration);
			sessionForm.setSessionName(sessionName);
			final ConferenceUser conferenceUser = this.conferenceUserService.getCurrentConferenceUser();
			Session session = sessionService.createOrUpdateSession(conferenceUser, sessionForm);
			String returnView = displayEditSessionForm(request, model, session.getSessionId(), null, null, null, true);
			return returnView;
		}

	    return "BlackboardVCPortlet_edit";
	}
	
    @RenderMapping(params="action=editSession")
    public String displayEditSessionForm(RenderRequest request, ModelMap model, @RequestParam long sessionId, @RequestParam(required = false) String presentationUploadError, @RequestParam(required = false) String multimediaUploadError, @RequestParam(required = false) String deleteMultimediaError, @RequestParam(value = "needToSendInitialEmail", defaultValue = "false", required = false) boolean needToSendInitialEmail) throws PortletModeException
	{
    	if(WindowState.NORMAL.equals(request.getWindowState())) {
	    	return viewController.view(request, model, null, null);
	    }
    	
    	final ServerConfiguration serverConfiguration = this.serverConfigurationService.getServerConfiguration();
        model.put("serverConfiguration", serverConfiguration);

		final Session session = this.sessionService.getSession(sessionId);
		//TODO if session is null

		// Don't override sessionForm if it already exists due to Form Validation
		if (!model.containsKey("sessionForm"))
		{
			SessionForm sessionForm = new SessionForm(session);
			sessionForm.setNeedToSendInitialEmail(needToSendInitialEmail);
			model.addAttribute("sessionForm", sessionForm);
		}

        final Set<ConferenceUser> sessionChairs = this.sessionService.getSessionChairs(session);
        model.addAttribute("sessionChairs", Ordering.from(ConferenceUserDisplayComparator.INSTANCE).sortedCopy(sessionChairs));
        
        final Set<ConferenceUser> sessionNonChairs = this.sessionService.getSessionNonChairs(session);
        model.addAttribute("sessionNonChairs", Ordering.from(ConferenceUserDisplayComparator.INSTANCE).sortedCopy(sessionNonChairs));
        
        final Set<Multimedia> sessionMultimedia = this.sessionService.getSessionMultimedia(session);
        model.addAttribute("sessionMultimedia", Ordering.from(MultimediaDisplayComparator.INSTANCE).sortedCopy(sessionMultimedia));
        
        model.addAttribute("presentationFileTypes", presentationFileTypes);
		model.addAttribute("presentation", session.getPresentation());

		model.addAttribute("multimediaFileTypes", multimediaFileTypes);

		if (presentationUploadError != null)
		{
			model.addAttribute("presentationUploadError", presentationUploadError);
		}

		if (multimediaUploadError != null)
		{
			model.addAttribute("multimediaUploadError", multimediaUploadError);
		}

		if (deleteMultimediaError != null)
		{
			model.addAttribute("deleteMultimediaError", deleteMultimediaError);
		}

        return "BlackboardVCPortlet_edit";
    }
	
	@ActionMapping(params = "action=saveSession")
	public void saveSession(ActionResponse response, @Valid SessionForm session, BindingResult bindingResult) throws PortletModeException
	{
		if (bindingResult.hasErrors())
		{
			response.setRenderParameter("sessionId", Long.toString(session.getSessionId()));
			response.setRenderParameter("action", "editSession");
			response.setPortletMode(PortletMode.EDIT);
		}
		else
		{
			final ConferenceUser conferenceUser = this.conferenceUserService.getCurrentConferenceUser();
			this.sessionService.createOrUpdateSession(conferenceUser, session);
			response.setPortletMode(PortletMode.VIEW);
		}
	}
    
    @ActionMapping(params = "action=deleteSessions")
    public void deleteSession(ActionResponse response, Locale locale, @RequestParam(required = false) long[] deleteSession) throws PortletModeException {

		if (deleteSession == null)
		{
			response.setRenderParameter("deleteSessionError", messageSource.getMessage("error.nothingselected", null, locale));
		}
		else
		{
			//TODO do in try/catch?
			for (final long sessionId : deleteSession) {
				this.sessionService.removeSession(sessionId);
			}
		}

        response.setPortletMode(PortletMode.VIEW);
    }

    @ActionMapping(params = "action=Add Moderator")
    public void addSessionChair(ActionResponse response, @Valid AddModeratorForm addModeratorForm, BindingResult bindingResult) throws PortletModeException {

		if (!bindingResult.hasErrors())
		{
			String displayName = StringUtils.trimToNull(addModeratorForm.getModeratorName());
			String email = StringUtils.trimToNull(addModeratorForm.getEmailAddress());

			this.sessionService.addSessionChair(addModeratorForm.getSessionId(), displayName, email, addModeratorForm.isNeedToSendInitialEmail());
		}

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("action", "editSession");
        response.setRenderParameter("sessionId", Long.toString(addModeratorForm.getSessionId()));
        response.setRenderParameter("needToSendInitialEmail", Boolean.toString(addModeratorForm.isNeedToSendInitialEmail()));
    }

    @ActionMapping(params = "action=Delete Moderator(s)")
    public void deleteSessionChairs(ActionResponse response, @Valid DeleteModeratorsForm deleteModeratorsForm, BindingResult bindingResult) throws PortletModeException {

		if (!bindingResult.hasErrors())
		{
			this.sessionService.removeSessionChairs(deleteModeratorsForm.getDeleteModeratorSessionId(), deleteModeratorsForm.isNeedToSendInitialEmail(), deleteModeratorsForm.getChairId());
		}

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("action", "editSession");
        response.setRenderParameter("sessionId", Long.toString(deleteModeratorsForm.getDeleteModeratorSessionId()));
        response.setRenderParameter("needToSendInitialEmail", Boolean.toString(deleteModeratorsForm.isNeedToSendInitialEmail()));
    }

    @ActionMapping(params = "action=Add Participant")
    public void addSessionNonChair(ActionResponse response, @Valid AddParticipantForm participantForm, BindingResult bindingResult) throws PortletModeException {

		if (!bindingResult.hasErrors())
		{
			final String displayName = StringUtils.trimToNull(participantForm.getParticipantName());
			final String email = StringUtils.trimToNull(participantForm.getEmailAddress());
			this.sessionService.addSessionNonChair(participantForm.getSessionId(), displayName, email, participantForm.isNeedToSendInitialEmail());
		}

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("action", "editSession");
        response.setRenderParameter("sessionId", Long.toString(participantForm.getSessionId()));
        response.setRenderParameter("needToSendInitialEmail", Boolean.toString(participantForm.isNeedToSendInitialEmail()));
    }

    @ActionMapping(params = "action=Delete Participant(s)")
    public void deleteSessionNonChairs(ActionResponse response, @Valid DeleteParticipantsForm deleteParticipantsForm, BindingResult bindingResult) throws PortletModeException {

		if (!bindingResult.hasErrors())
		{
			this.sessionService.removeSessionNonChairs(deleteParticipantsForm.getDeleteParticipantsSessionId(), deleteParticipantsForm.isNeedToSendInitialEmail(), deleteParticipantsForm.getNonChairId());
		}

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("action", "editSession");
        response.setRenderParameter("sessionId", Long.toString(deleteParticipantsForm.getDeleteParticipantsSessionId()));
        response.setRenderParameter("needToSendInitialEmail", Boolean.toString(deleteParticipantsForm.isNeedToSendInitialEmail()));
    }
    
    @ActionMapping(params = "action=Upload Multimedia")
    public void uploadMultimedia(ActionResponse response, Locale locale, @RequestParam long sessionId, @RequestParam MultipartFile multimediaUpload, @RequestParam boolean needToSendInitialEmail) throws PortletModeException
	{
		String fileExtension = StringUtils.substringAfter(multimediaUpload.getOriginalFilename(), ".").toLowerCase();

		// Validate
		if (multimediaUpload.getSize() < 1)
		{
			response.setRenderParameter("multimediaUploadError", messageSource.getMessage("error.uploadfilenotselected", null, locale));
		}
		else if (multimediaUpload.getSize() > maxFileUploadSize)
		{
			response.setRenderParameter("multimediaUploadError", messageSource.getMessage("error.uploadfilesizetoobig", null, locale));
		}
		else if (fileExtension.length() == 0 || !multimediaFileTypes.contains(fileExtension))
		{
			response.setRenderParameter("multimediaUploadError", messageSource.getMessage("error.uploadfileextensionswrong", null, locale));
		}
		else
		{
			this.sessionService.addMultimedia(sessionId, multimediaUpload);
		}

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("sessionId", Long.toString(sessionId));
		response.setRenderParameter("action", "editSession");
		response.setRenderParameter("needToSendInitialEmail", Boolean.toString(needToSendInitialEmail));
	}

    @ActionMapping(params = "action=Delete Multimedia Item(s)")
    public void deleteMultimedia(ActionResponse response, Locale locale, @RequestParam long sessionId, @RequestParam(required = false) long[] deleteMultimedia, @RequestParam boolean needToSendInitialEmail) throws PortletModeException
	{
		if (deleteMultimedia == null)
		{
			response.setRenderParameter("deleteMultimediaError", messageSource.getMessage("error.nothingselected", null, locale));
		}
		else
		{
			this.sessionService.deleteMultimedia(sessionId, deleteMultimedia);
		}

		response.setPortletMode(PortletMode.EDIT);
		response.setRenderParameter("sessionId", Long.toString(sessionId));
		response.setRenderParameter("action", "editSession");
		response.setRenderParameter("needToSendInitialEmail", Boolean.toString(needToSendInitialEmail));
	}
    
    @ActionMapping(params = "action=Upload Presentation")
    public void uploadPresentation(ActionResponse response, Locale locale, @RequestParam long sessionId, @RequestParam MultipartFile presentationUpload, @RequestParam boolean needToSendInitialEmail) throws PortletModeException
	{
		String fileExtension = StringUtils.substringAfter(presentationUpload.getOriginalFilename(), ".").toLowerCase();

		// Validate
		if (presentationUpload.getSize() < 1)
		{
			response.setRenderParameter("presentationUploadError", messageSource.getMessage("error.uploadfilenotselected", null, locale));
		}
		else if (presentationUpload.getSize() > maxFileUploadSize)
		{
			response.setRenderParameter("presentationUploadError", messageSource.getMessage("error.uploadfilesizetoobig", null, locale));
		}
		else if (fileExtension.length() == 0 || !presentationFileTypes.contains(fileExtension))
		{
			response.setRenderParameter("presentationUploadError", messageSource.getMessage("error.uploadfileextensionswrong", null, locale));
		}
		else
		{
			this.sessionService.addPresentation(sessionId, presentationUpload);
		}

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("sessionId", Long.toString(sessionId));
		response.setRenderParameter("action", "editSession");
		response.setRenderParameter("needToSendInitialEmail", Boolean.toString(needToSendInitialEmail));
	}

    @ActionMapping(params = "action=Delete Presentation")
    public void deletePresentation(ActionResponse response, @RequestParam long sessionId, @RequestParam boolean needToSendInitialEmail) throws PortletModeException
	{
        this.sessionService.deletePresentation(sessionId);

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("sessionId", Long.toString(sessionId));
		response.setRenderParameter("action", "editSession");
		response.setRenderParameter("needToSendInitialEmail", Boolean.toString(needToSendInitialEmail));
    }
}
