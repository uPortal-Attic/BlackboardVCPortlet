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
package org.jasig.portlet.blackboardvcportlet.mvc.callback;

import com.elluminate.sas.BasicAuth;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;

/**
 * Controller for session finish callback.
 * @author Richard Good
 */
@Controller
@RequestMapping("VIEW")
public class BlackboardVCPortletCallbackController implements ServletContextAware
{
	private static final Logger logger = LoggerFactory.getLogger(BlackboardVCPortletCallbackController.class);

    private static ServletContext servletContext;
    
    @Autowired
    RecordingService recordingService;
    
    @Value("${bbc.username}")
    private String username;
    
    @Value("${bbc.password}")
    private String password;
    
    /**
     * Callback method. Looks for a passed session_id and updates the recordings
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    @ResourceMapping(value = "callback")
    public ModelAndView callback(@RequestParam String sessionId, @RequestParam String roomClosedMillis, @RequestParam String recPlaybackLink, @RequestParam String roomOpenedMillis) throws Exception {
    
        /*logger.info("callback called from remote host: "+request.getRemoteHost());
        
        String sessionId = request.getParameter("session_id");
        String roomClosedMillis = request.getParameter("room_closed_millis");
        String recPlaybackLink = request.getParameter("rec_playback_link");
        String roomOpenedMillis = request.getParameter("room_opened_millis");*/
        
        logger.debug("sessionId:"+sessionId);
        logger.debug("roomClosedMillis:"+roomClosedMillis);
        logger.debug("recPlaybackLink:"+recPlaybackLink);
        logger.debug("roomOpenedMillis:"+roomOpenedMillis);
        
        logger.debug("updating session recordings");
        BasicAuth user = new BasicAuth();
        
        user.setName(username);
        user.setPassword(password);
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
