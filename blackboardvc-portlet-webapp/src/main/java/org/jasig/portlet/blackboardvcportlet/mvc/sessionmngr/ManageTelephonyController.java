package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.RenderRequest;
import javax.validation.Valid;

import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;
import org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr.forms.TelephonyForm;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.jboss.logging.MessageBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.ws.soap.client.SoapFaultClientException;

@Controller
@RequestMapping("EDIT")
public class ManageTelephonyController {
	
	private SessionService sessionService;

	
	@Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

	@RenderMapping(params = "action=configureTelephony")
	public String manageTelephony(@RequestParam long sessionId, @RequestParam(required=false) String error, RenderRequest request, ModelMap model) {
		Session session = sessionService.getSession(sessionId);
		if(!model.containsAttribute("telephonyForm")) {
			SessionTelephony telephony = sessionService.getSessionTelephony(session);
			if(null == telephony) {
				telephony = new TelephonyForm(sessionId);
			}
			
			model.addAttribute("telephonyForm",telephony);
		}
		
		model.addAttribute("session",session);
		model.addAttribute("error", error);
		return "configureTelephony";
	}
	
	@ActionMapping(params = "action=saveTelephony")
	public void saveTelephony(@Valid TelephonyForm telephonyForm, BindingResult bindingResult, ActionResponse response, ActionRequest request) throws PortletModeException {
		//setting response param
		response.setRenderParameter("sessionId", Long.toString(telephonyForm.getSessionId()));
		
		if(bindingResult.hasErrors()) {
			response.setPortletMode(PortletMode.EDIT);
			response.setRenderParameter("action", "configureTelephony");
		} else {
			try {
				//save telephony information
				sessionService.createOrUpdateSessionTelephony(telephonyForm.getSessionId(), telephonyForm);
				//setup response redirect
				response.setPortletMode(PortletMode.VIEW);
		        response.setRenderParameter("action", "viewSession");
			} catch (SoapFaultClientException ex) {
				if("Telephony is not enabled".equalsIgnoreCase(ex.getMessage())) {
					response.setRenderParameter("errorCode", "error.thirdPartyDisabled");
				} else {
				    //use the error message sent from BBC. In theory we shouldn't get here but we might.
				    response.setRenderParameter("error", ex.getMessage());
				}
				
                response.setPortletMode(PortletMode.EDIT);
                response.setRenderParameter("action", "configureTelephony");
			}
			
		}
	}
	
	@ActionMapping(params = "action=deleteTelephony")
	public void removeTelephony(@RequestParam long sessionId, ActionResponse response) throws PortletModeException {
		sessionService.deleteSessionTelephony(sessionId);
		response.setRenderParameter("sessionId", Long.toString(sessionId));
		response.setPortletMode(PortletMode.VIEW);
        response.setRenderParameter("action", "viewSession");
	}
}
