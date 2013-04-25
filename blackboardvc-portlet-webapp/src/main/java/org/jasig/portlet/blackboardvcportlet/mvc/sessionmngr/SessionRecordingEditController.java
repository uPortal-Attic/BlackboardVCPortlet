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

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;

import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Controller class for Portlet EDIT related actions and render
 *
 * @author Richard Good
 */
@Controller
@RequestMapping("EDIT")
public class SessionRecordingEditController
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private RecordingService recordingService;

	@Autowired
	public void setRecordingService(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @RenderMapping(params="action=editRecording")
    public String displayEditSessionForm(ModelMap model, @RequestParam long recordingId) throws PortletModeException
	{
        final SessionRecording sessionRecording = this.recordingService.getSessionRecording(recordingId);
        
        model.put("recording", sessionRecording); 
        
        return "BlackboardVCPortlet_editRecording";
    }
    
    @ActionMapping(params = "action=saveRecording")
    public void deleteSession(ActionResponse response, @RequestParam long recordingId, @RequestParam String roomName) throws PortletModeException {
        this.recordingService.updateSessionRecordingName(recordingId, roomName);
        
        response.setPortletMode(PortletMode.VIEW);
    }
}
