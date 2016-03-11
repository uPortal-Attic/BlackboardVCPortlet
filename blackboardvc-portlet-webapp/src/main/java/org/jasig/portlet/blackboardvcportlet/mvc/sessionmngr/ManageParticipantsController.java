/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.data.BasicUser;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.AddParticipantForm;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.DeleteParticipantsForm;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.UpdateParticipantForm;
import org.jasig.portlet.blackboardvcportlet.security.ConferenceUserService;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.google.common.collect.ImmutableSortedSet;

@Controller
@RequestMapping("EDIT")
public class ManageParticipantsController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SessionService sessionService;
    private ViewSessionListController viewController;
    private ConferenceUserService conferenceUserService;
    
    @Autowired
    public void setConferenceUserService(ConferenceUserService conferenceUserService) {
        this.conferenceUserService = conferenceUserService;
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    
    @Autowired
    public void setViewController(ViewSessionListController viewController) {
        this.viewController = viewController;
    }

    @RenderMapping(params = "action=addParticipants")
    public String viewParticipants(PortletRequest request, @RequestParam long sessionId, ModelMap model) {
        if (WindowState.NORMAL.equals(request.getWindowState())) {
            return viewController.view(request, model, null, null);
        }
        
        final Session session = this.sessionService.getSession(sessionId);
        model.addAttribute("session", session);
        
        return "viewParticipants";
    }
    
    @ResourceMapping("getParticipants")
    public String getParticipants(@RequestParam long sessionId, ModelMap model) {
        final Session session = this.sessionService.getSession(sessionId);
        
        final Set<ConferenceUser> sessionChairs = this.sessionService.getSessionChairs(session);
        model.addAttribute("sessionChairs", ImmutableSortedSet.copyOf(ConferenceUserDisplayComparator.INSTANCE, sessionChairs));
        
        final Set<ConferenceUser> sessionNonChairs = this.sessionService.getSessionNonChairs(session);
        model.addAttribute("sessionNonChairs", ImmutableSortedSet.copyOf(ConferenceUserDisplayComparator.INSTANCE, sessionNonChairs));
        
        return "json";
    }
    
    @ResourceMapping("addParticipant")
    public String addParticipant(ResourceResponse response, ModelMap model, @Valid AddParticipantForm addParticipantForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");
            response.setProperty("X-Status-Reason", "Validation failed");
            
            final Map<String, String> fieldErrors = getFieldErrors(bindingResult);
            
            model.put("fieldErrors", fieldErrors);
        }
        else {
            final String uniqueId = StringUtils.trimToNull(addParticipantForm.getUniqueId());
            final String displayName = StringUtils.trimToNull(addParticipantForm.getName());
            final String email = StringUtils.trimToNull(addParticipantForm.getEmail());
            final ConferenceUser conferenceUser = this.conferenceUserService.getOrCreateConferenceUser(uniqueId, displayName, email);
            
            final ConferenceUser participant;
            if (addParticipantForm.isModerator()) {
                participant = this.sessionService.addSessionChair(addParticipantForm.getSessionId(), conferenceUser.getUserId());
            }
            else {
                participant = this.sessionService.addSessionNonChair(addParticipantForm.getSessionId(), conferenceUser.getUserId());
            }
            model.put("participant", participant);
        }
        
        return "json";
    }
    
    @ResourceMapping("updateParticipant")
    public String updateParticipant(ResourceResponse response, ModelMap model, @Valid UpdateParticipantForm updateParticipantForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");
            response.setProperty("X-Status-Reason", "Validation failed");
            
            final Map<String, String> fieldErrors = getFieldErrors(bindingResult);
            
            model.put("fieldErrors", fieldErrors);
        }
        else {
            final long sessionId = updateParticipantForm.getSessionId();
            final long userId = updateParticipantForm.getId();
            
            final ConferenceUser participant;
            if (updateParticipantForm.isModerator()) {
            	participant = this.sessionService.updateRole(sessionId, userId, ConferenceUser.Roles.CHAIR);
            }
            else {
            	participant = this.sessionService.updateRole(sessionId, userId, ConferenceUser.Roles.NONCHAIR);
            }
            
            model.put("participant", participant);
        }
        
        return "json";
    }
    
    @ResourceMapping("deleteParticipant")
    public String deleteParticipants(ResourceResponse response, ModelMap model, @Valid DeleteParticipantsForm deleteParticipantForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");
            response.setProperty("X-Status-Reason", "Validation failed");
            
            final Map<String, String> fieldErrors = getFieldErrors(bindingResult);
            
            model.put("fieldErrors", fieldErrors);
        }
        else {
            this.sessionService.removeSessionChairs(deleteParticipantForm.getSessionId(), deleteParticipantForm.getId());
            this.sessionService.removeSessionNonChairs(deleteParticipantForm.getSessionId(), deleteParticipantForm.getId());
        }
        
        return "json";
    }
    
    @ResourceMapping("searchForParticipants")
    public String searchForParticipants(ModelMap model, @RequestParam(required=false) String name, @RequestParam(required=false) String email) {
        //TODO per-user rate limiting
        final Set<BasicUser> result;
        if (name != null) {
            result = this.conferenceUserService.searchForBasicUserByName(name);
        }
        else if (email != null) {
            result = this.conferenceUserService.searchForBasicUserByEmail(email);
        }
        else {
            result = Collections.emptySet();
        }
        
        model.addAttribute("result", result);
        
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
