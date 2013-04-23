package org.jasig.portlet.blackboardvcportlet.mvc.admin;

import javax.portlet.PortletRequest;

import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;
import org.jasig.portlet.blackboardvcportlet.service.ServerConfigurationService;
import org.jasig.portlet.blackboardvcportlet.service.ServerQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller
@RequestMapping("VIEW")
public class ViewAdminServerConfigController {
	
	private ServerConfigurationService serverConfigService;
	private ServerQuotaService serverQuotaService;
	
	@Autowired
	public void setServerConfigurationService(ServerConfigurationService service) {
		this.serverConfigService = service;
	}
	
	@Autowired
	public void setServerQuota(ServerQuotaService service) {
		this.serverQuotaService = service;
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
}
