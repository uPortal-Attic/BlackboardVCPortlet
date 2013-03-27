package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.jasig.portlet.blackboardvcportlet.service.AuthorisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("authorizationService")
public class AuthorisationServiceImpl implements AuthorisationService {
	private static final Logger logger = LoggerFactory.getLogger(AuthorisationService.class);

    /**
     * Is the user in the admin group
     * @param request
     * @return boolean
     */
    public boolean isAdminAccess(RenderRequest request)
    {
        return request.isUserInRole(getAdminRole(request));
    }
    
    /**
     * Gets the admin group name
     * @param request
     * @return String
     */
    public String getAdminRole(RenderRequest request)
    {
        final PortletPreferences prefs=request.getPreferences();
        return prefs.getValue("adminRole", null);
    }
    
    /**
     * Is the user allowed full access to the portlet
     * @param request
     * @return boolean
     */
    public boolean isFullAccess(RenderRequest request)
    {
        logger.debug("isFullAccess called");
        String attributeValue = getUserInfo(request).get(this.getUserAttribute(request));
        logger.debug("attributeValue:"+attributeValue);
        logger.debug("full access requires:"+getFullAccessValues(request));
        if (getFullAccessValues(request).indexOf(attributeValue)!=-1)
        {
            logger.debug("giving full access");
            return true;
        }
        else
        {
            logger.debug("giving basic acccess");
            return false;
        }
    }
    
    /**
     * Gets the user info Map from the request
     * @param request
     * @return 
     */
    public Map<String,String> getUserInfo(RenderRequest request)
    {
        return (Map<String,String>) request.getAttribute(PortletRequest.USER_INFO);
    }
    
    /**
     * Gets the userTypeAttribute from the request
     * @param request
     * @return String
     */
    public String getUserAttribute(RenderRequest request)
    {
        final PortletPreferences prefs=request.getPreferences();
        return prefs.getValue("userTypeAttribute", null);
    }
    
    /**
     * Gets the values required for full access
     * @param request
     * @return String
     */
    public String getFullAccessValues(RenderRequest request)
    {
        final PortletPreferences prefs=request.getPreferences();
        return prefs.getValue("fullAccessValues", null);
    }
}
