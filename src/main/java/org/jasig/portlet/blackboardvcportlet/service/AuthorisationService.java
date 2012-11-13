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
package org.jasig.portlet.blackboardvcportlet.service;

import java.util.Map;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * Service Class which provides authorisation level methods
 * @author Richard Good
 */
@Service
public class AuthorisationService {
    
    protected final Log logger = LogFactory.getLog(AuthorisationService.class);
    
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
