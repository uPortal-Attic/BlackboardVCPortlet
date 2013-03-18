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

import com.elluminate.sas.BasicAuth;
import com.elluminate.sas.GetServerConfigurationResponseCollection;
import com.elluminate.sas.ServerConfigurationResponse;
import org.jasig.portlet.blackboardvcportlet.dao.ServerConfigurationDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import javax.portlet.PortletPreferences;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service Class for Server Configuration
 * @author rgood
 */
@Service
public class ServerConfigurationService
{
	private static final Logger logger = LoggerFactory.getLogger(ServerConfigurationService.class);
	private boolean isInit = false;
    private BasicAuth user;
    @Autowired
    ServerConfigurationDao serverConfigurationDao;

	@Autowired
	private WebServiceTemplate webServiceTemplate;

    /**
     * Gets the server configuration
     * @param prefs
     * @return 
     */
    public ServerConfiguration getServerConfiguration(PortletPreferences prefs) {
        refreshServerConfiguration(prefs);
        return serverConfigurationDao.getServerConfiguration();
    }

    /**
     * Stores the server configuration
     * @param serverConfiguration 
     */
    public void storeServerConfiguration(ServerConfiguration serverConfiguration) {
        serverConfigurationDao.deleteServerConfiguration();
        serverConfigurationDao.saveServerConfiguration(serverConfiguration);
    }

    /**
     * Refreshes the server configuration, only updates local cache if last update
     * was older than an hour.
     * @param prefs 
     */
    public void refreshServerConfiguration(PortletPreferences prefs) {
        // Quota will refresh on the hour
        ServerConfiguration serverConfiguration = serverConfigurationDao.getServerConfiguration();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date date = calendar.getTime();

		if (serverConfiguration == null || serverConfiguration.getLastUpdated().before(date))
		{

			logger.debug("refreshing server configuration");
			if (!this.isInit())
			{
				doInit(prefs);
			}

			try
			{ // Call Web Service Operation

				GetServerConfigurationResponseCollection responseCollection = (GetServerConfigurationResponseCollection) webServiceTemplate.marshalSendAndReceive(new com.elluminate.sas.ServerConfiguration());
				List<ServerConfigurationResponse> configResult = responseCollection.getServerConfigurationResponses();

				this.logger.debug("Result = " + configResult);
				for (ServerConfigurationResponse response : configResult)
				{
					ServerConfiguration configuration = new ServerConfiguration();
					configuration.setBoundaryTime(response.getBoundaryTime());
					configuration.setLastUpdated(new Date());
					configuration.setMaxAvailableCameras(response.getMaxAvailableCameras());
					configuration.setMaxAvailableTalkers(response.getMaxAvailableTalkers());
					if (response.isMayUseSecureSignOn())
					{
						configuration.setMayUseSecureSignOn('Y');
					} else
					{
						configuration.setMayUseSecureSignOn('N');
					}

					if (response.isMustReserveSeats())
					{
						configuration.setMustReserveSeats('Y');
					} else
					{
						configuration.setMustReserveSeats('N');
					}

					if (response.isRaiseHandOnEnter())
					{
						configuration.setRaiseHandOnEnter('Y');
					} else
					{
						configuration.setRaiseHandOnEnter('N');
					}

					configuration.setTimezone(response.getTimeZone());

					this.storeServerConfiguration(configuration);

				}
			}
			catch (Exception ex)
			{
				this.logger.error(ex.toString());
			}
		} else
		{
			logger.debug("Configuration doesn't need refreshed.");
		}
	}
    
    private boolean isInit() {
        return this.isInit;
    }

    /**
     * Init method for basic auth user.
     * @param prefs 
     */
    private void doInit(PortletPreferences prefs) {
        logger.debug("doInit called");
        user = new BasicAuth();
        user.setName(prefs.getValue("wsusername", null));
        user.setPassword(prefs.getValue("wspassword", null));
        isInit = true;
    }
}
