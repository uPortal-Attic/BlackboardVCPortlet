package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.DeleteMediaFilesForm;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.google.common.collect.ImmutableSortedSet;

@Controller
@RequestMapping("EDIT")
public class ManageMediaFilesController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionService sessionService;
    private SessionDao sessionDao;
    private ViewSessionListController viewController;
    private MessageSource messageSource;
    private String multimediaFileTypes;
    private Integer maxFileUploadSize;

    @Autowired
    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Value("${multimediaFileTypes}")
    public void setMultimediaFileTypes(String multimediaFileTypes) {
        this.multimediaFileTypes = multimediaFileTypes;
    }

    @Value("${maxuploadsize}")
    public void setMaxFileUploadSize(Integer maxFileUploadSize) {
        this.maxFileUploadSize = maxFileUploadSize;
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Autowired
    public void setViewController(ViewSessionListController viewController) {
        this.viewController = viewController;
    }

    @RenderMapping(params = "action=manageMediaFiles")
    public String manageMultimedia(PortletRequest request, @RequestParam long sessionId, @RequestParam(required=false, value="multimediaUploadError") String multimediaUploadError, ModelMap model) {
        if (WindowState.NORMAL.equals(request.getWindowState())) {
            return viewController.view(request, model, null, null);
        }

        model.addAttribute("multimediaFileTypes", multimediaFileTypes);
        
        if(multimediaUploadError != null && multimediaUploadError.length() > 0) {
        	model.addAttribute("multimediaUploadError",multimediaUploadError);
        }

        final Session session = this.sessionService.getSession(sessionId);
        model.addAttribute("session", session);

        return "manageMultimedia";
    }

    @ResourceMapping("getMediaFiles")
    public String getParticipants(@RequestParam long sessionId, ModelMap model) {
        final Session session = this.sessionService.getSession(sessionId);

        final Set<Multimedia> sessionMultimedias = this.sessionDao.getSessionMultimedias(session);
        model.addAttribute("multimedias", ImmutableSortedSet.copyOf(MultimediaDisplayComparator.INSTANCE, sessionMultimedias));

        return "json";
    }

    @ActionMapping("uploadMediaFile")
    public void uploadMultimedia(ActionResponse response, Locale locale, @RequestParam long sessionId, @RequestParam MultipartFile multimediaUpload) throws PortletModeException {
        final String fileExtension = FilenameUtils.getExtension(multimediaUpload.getOriginalFilename());

        // Validate
        if (multimediaUpload.getSize() < 1) {
            response.setRenderParameter("multimediaUploadError", messageSource.getMessage("error.uploadfilenotselected", null, locale));
        } else if (multimediaUpload.getSize() > maxFileUploadSize) {
            response.setRenderParameter("multimediaUploadError", messageSource.getMessage("error.uploadfilesizetoobig", null, locale));
        } else if (fileExtension.length() == 0 || !multimediaFileTypes.contains(fileExtension.toLowerCase())) {
            response.setRenderParameter("multimediaUploadError", messageSource.getMessage("error.uploadfileextensionswrong", null, locale));
        } else {
            this.sessionService.addMultimedia(sessionId, multimediaUpload);
        }

        response.setPortletMode(PortletMode.EDIT);
        response.setRenderParameter("sessionId", Long.toString(sessionId));
        response.setRenderParameter("action", "manageMediaFiles");
    }

    @ResourceMapping("deleteMediaFile")
    public String deleteMultimedia(ResourceResponse response, ModelMap model,
            @Valid DeleteMediaFilesForm deleteMediaFilesForm,
            BindingResult bindingResult)
            throws PortletModeException {
        
        if (bindingResult.hasErrors()) {
            response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");
            response.setProperty("X-Status-Reason", "Validation failed");

            final Map<String, String> fieldErrors = getFieldErrors(bindingResult);

            model.put("fieldErrors", fieldErrors);
        }
        else {
            this.sessionService.deleteMultimedia(deleteMediaFilesForm.getSessionId(), deleteMediaFilesForm.getId());
        }

        return "json";
    }


    protected Map<String, String> getFieldErrors(BindingResult bindingResult) {
        final Map<String, String> fieldErrors = new LinkedHashMap<String, String>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return fieldErrors;
    }
}
