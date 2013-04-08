package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ServerConfigurationDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.service.ServerConfigurationService;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardGetServerConfigurationResponseCollection;
import com.elluminate.sas.BlackboardServerConfiguration;
import com.elluminate.sas.BlackboardServerConfigurationResponse;
import com.elluminate.sas.ObjectFactory;

@Service("serverConfigurationService")
public class ServerConfigurationServiceImpl implements ServerConfigurationService
{
	private static final Logger logger = LoggerFactory.getLogger(ServerConfigurationServiceImpl.class);

    private ServerConfigurationDao serverConfigurationDao;
	private SASWebServiceOperations sasWebServiceTemplate;

	@Autowired
	public void setServerConfigurationDao(ServerConfigurationDao serverConfigurationDao)
	{
		this.serverConfigurationDao = serverConfigurationDao;
	}

	@Autowired
	public void setSasWebServiceTemplate(SASWebServiceOperations sasWebServiceTemplate)
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

    @Scheduled(fixedRate=3600000)
    public void scheduledRefreshServerConfiguration() {
        this.refreshServerConfiguration();
    }

    /**
     * Refreshes the server configuration, only updates local cache if last update
     * was older than an hour.
     * @param prefs PortletPreferences
     */
    public ServerConfiguration refreshServerConfiguration() {
        final ServerConfiguration serverConfiguration = serverConfigurationDao.getServerConfiguration();
        if (serverConfiguration != null && serverConfiguration.getLastUpdated().plusHours(1).isAfterNow()) {
            //Nothing to do, serverConfiguration exists and is less than 1 hour old
            return serverConfiguration;
        }
		logger.debug("refreshing server configuration");
		try
		{ // Call Web Service Operation
		    final JAXBElement<BlackboardServerConfiguration> request = new ObjectFactory().createGetServerConfiguration(null);

		    BlackboardGetServerConfigurationResponseCollection responseCollection = (BlackboardGetServerConfigurationResponseCollection) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/GetServerConfiguration", request);
			List<BlackboardServerConfigurationResponse> configResult = responseCollection.getServerConfigurationResponses();

			logger.debug("Result = " + configResult);
			for (BlackboardServerConfigurationResponse response : configResult) {
			    return this.serverConfigurationDao.createOrUpdateConfiguration(response);
			}
		}
		catch (Exception ex)
		{
			logger.error("Failed to refresh ServerConfiguration", ex);
		}
		
		return null;
	}
}
