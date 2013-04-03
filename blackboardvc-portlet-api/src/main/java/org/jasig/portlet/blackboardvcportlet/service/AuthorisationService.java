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

import javax.portlet.PortletRequest;

/**
 * Service Class which provides authorisation level methods
 * @author Richard Good
 */

public interface AuthorisationService {
    /**
     * Is the user in the admin group
     * @param request
     * @return boolean
     */
    public boolean isAdminAccess(PortletRequest request);
    
    /**
     * Gets the admin group name
     * @param request
     * @return String
     */
    public String getAdminRole(PortletRequest request);
    
    /**
     * Is the user allowed full access to the portlet
     * @param request
     * @return boolean
     */
    public boolean isFullAccess(PortletRequest request);
    
    /**
     * Gets the user info Map from the request
     * @param request
     * @return 
     */
    public Map<String,String> getUserInfo(PortletRequest request);
    
    /**
     * Gets the userTypeAttribute from the request
     * @param request
     * @return String
     */
    public String getUserAttribute(PortletRequest request);
    
    /**
     * Gets the values required for full access
     * @param request
     * @return String
     */
    public String getFullAccessValues(PortletRequest request);
    
}
