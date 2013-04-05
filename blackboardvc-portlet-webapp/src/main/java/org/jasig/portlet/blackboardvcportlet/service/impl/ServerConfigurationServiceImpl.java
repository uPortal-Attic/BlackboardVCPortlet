package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import org.jasig.portlet.blackboardvcportlet.dao.ServerConfigurationDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.service.ServerConfigurationService;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.elluminate.sas.GetServerConfigurationResponseCollection;
import com.elluminate.sas.ServerConfigurationResponse;

@Service("serverConfigurationService")
public class ServerConfigurationServiceImpl implements ServerConfigurationService
{
	private static final Logger logger = LoggerFactory.getLogger(ServerConfigurationServiceImpl.class);

    private ServerConfigurationDao serverConfigurationDao;
	private SASWebServiceTemplate sasWebServiceTemplate;

	@Autowired
	public void setServerConfigurationDao(ServerConfigurationDao serverConfigurationDao)
	{
		this.serverConfigurationDao = serverConfigurationDao;
	}

	@Autowired
	public void setSasWebServiceTemplate(SASWebServiceTemplate sasWebServiceTemplate)
	{
		this.sasWebServiceTemplate = sasWebServiceTemplate;
	}

	/**
     * Gets the server configuration
     * @param prefs PortletPreferences
     * @return ServerConfiguration
     */
    public ServerConfiguration getServerConfiguration() {
        ServerConfiguration serverConfiguration = serverConfigurationDao.getServerConfiguration();
        if (serverConfiguration == null) {
            serverConfiguration = this.refreshServerConfiguration();
        }
        return serverConfiguration;
    }


    /**
     * Refreshes the server configuration, only updates local cache if last update
     * was older than an hour.
     * @param prefs PortletPreferences
     */
    @Scheduled(fixedRate=3600000)
    public ServerConfiguration refreshServerConfiguration() {
        final ServerConfiguration serverConfiguration = serverConfigurationDao.getServerConfiguration();
        if (serverConfiguration != null && serverConfiguration.getLastUpdated().plusHours(1).isAfterNow()) {
            //Nothing to do, serverConfiguration exists and is less than 1 hour old
            return serverConfiguration;
        }
		logger.debug("refreshing server configuration");
		try
		{ // Call Web Service Operation
			com.elluminate.sas.ServerConfiguration sc = new com.elluminate.sas.ServerConfiguration();

			GetServerConfigurationResponseCollection responseCollection = (GetServerConfigurationResponseCollection) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/GetServerConfiguration", sc);
			List<ServerConfigurationResponse> configResult = responseCollection.getServerConfigurationResponses();

			logger.debug("Result = " + configResult);
			for (ServerConfigurationResponse response : configResult) {
			    return this.serverConfigurationDao.createOrUpdateConfiguration(response);
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.toString());
		}
		
		return null;
	}
}
