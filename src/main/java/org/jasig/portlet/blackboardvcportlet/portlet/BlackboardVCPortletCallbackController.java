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
package org.jasig.portlet.blackboardvcportlet.portlet;

import com.elluminate.sas.BasicAuth;
import java.util.Iterator;
import java.util.Map;
import javax.portlet.PortletPreferences;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;

/**
 * Controller for session finish callback.
 * @author Richard Good
 */
@Controller
public class BlackboardVCPortletCallbackController implements ServletContextAware {
    
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    private static ServletContext servletContext;
    
    @Autowired
    RecordingService recordingService;
    
    /**
     * Callback method. Looks for a passed session_id and updates the recordings
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/callback")
    public ModelAndView callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
        logger.info("callback called from remote host: "+request.getRemoteHost());
        
        String sessionId = request.getParameter("session_id");
        String roomClosedMillis = request.getParameter("room_closed_millis");
        String recPlaybackLink = request.getParameter("rec_playback_link");
        String roomOpenedMillis = request.getParameter("room_opened_millis");
        
        logger.debug("sessionId:"+sessionId);
        logger.debug("roomClosedMillis:"+roomClosedMillis);
        logger.debug("recPlaybackLink:"+recPlaybackLink);
        logger.debug("roomOpenedMillis:"+roomOpenedMillis);
        
        logger.debug("updating session recordings");
        BasicAuth user = new BasicAuth();
        user.setName((String)servletContext.getInitParameter("wsusername"));
        user.setPassword((String)servletContext.getInitParameter("wspassword"));
        recordingService.updateSessionRecordings(user,Long.valueOf(sessionId));
        logger.debug("done update");
        ModelAndView modelAndView = new ModelAndView("callback");
        
        return modelAndView;
        
    }
    
    @Override
    public void setServletContext(ServletContext sc) {
        servletContext=sc;
    }
}
