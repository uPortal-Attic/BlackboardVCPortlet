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
package org.jasig.portlet.blackboardvcportlet.mvc.admin;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;

import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.jasig.portlet.blackboardvcportlet.service.ServerConfigurationService;
import org.jasig.portlet.blackboardvcportlet.service.ServerQuotaService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class ViewAdminServerConfigController {
	
	private ServerConfigurationService serverConfigService;
	private ServerQuotaService serverQuotaService;
	private RecordingService recordingService;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public void setServerConfigurationService(ServerConfigurationService service) {
		this.serverConfigService = service;
	}
	
	@Autowired
	public void setServerQuota(ServerQuotaService service) {
		this.serverQuotaService = service;
	}
	
	@Autowired
	public void setRecordingService(RecordingService service) {
	    this.recordingService = service;
	}
	
	@RenderMapping
	public String view(PortletRequest request, ModelMap model) {
		
		//get server configuration
		ServerConfiguration serverConfiguration = serverConfigService.getServerConfiguration();
		model.addAttribute("serverConfiguration", serverConfiguration);
		//get quota information
		ServerQuota serverQuota = serverQuotaService.getServerQuota();
		model.addAttribute("serverQuota",serverQuota);
		
		//TODO get and display Blackboard web service version information
		
		return "admin_server_information";
	}
	
	@ResourceMapping("datafixRecording")
	public String datafixRecording(PortletRequest request, ResourceResponse response, ModelMap model) {
	    String startDate = (String) request.getParameter("startDate");
	    String endDate = (String) request.getParameter("endDate");
	    
	    DateTime sd = DateTime.parse(startDate, DateTimeFormat.forPattern("MM-dd-YYYY"));
	    DateTime ed = DateTime.parse(endDate, DateTimeFormat.forPattern("MM-dd-YYYY"));
	    
	    if(sd != null && ed != null) {
	        int errd = recordingService.datafixRecordings(sd, ed);
	        if(errd > 0)
	            logger.warn("During datafixRecording, " + errd + " failed to insert");
	    } else {
	        response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "400");
            response.setProperty("X-Status-Reason", "Validation failed");
	    }
	    return "json";
	}
}
