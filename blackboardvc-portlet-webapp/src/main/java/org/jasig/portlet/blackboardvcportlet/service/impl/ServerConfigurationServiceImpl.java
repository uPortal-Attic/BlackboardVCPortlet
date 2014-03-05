package org.jasig.portlet.blackboardvcportlet.service.impl;

import org.jasig.portlet.blackboardvcportlet.dao.ServerConfigurationDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.GlobalSettingsWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.service.ServerConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("serverConfigurationService")
public class ServerConfigurationServiceImpl implements ServerConfigurationService
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ServerConfigurationDao serverConfigurationDao;
	private GlobalSettingsWSDao globalSettingsWSDao;

	@Autowired
	public void setServerConfigurationDao(ServerConfigurationDao serverConfigurationDao)
	{
		this.serverConfigurationDao = serverConfigurationDao;
	}

	@Autowired
	public void setGlobalSettingWSDao(GlobalSettingsWSDao value) {
		this.globalSettingsWSDao = value;
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
        final ServerConfiguration currentServerConfiguration = serverConfigurationDao.getServerConfiguration();
        if (currentServerConfiguration != null && currentServerConfiguration.getLastUpdated().plusHours(1).isAfterNow()) {
            //Nothing to do, serverConfiguration exists and is less than 1 hour old
            return currentServerConfiguration;
        }
        
		try {
			final ServerConfiguration newServerConfiguration = this.serverConfigurationDao.createOrUpdateConfiguration(globalSettingsWSDao.getServerConfiguration());
			if(currentServerConfiguration == null || currentServerConfiguration.getRandomCallbackUrl() == null) {
				globalSettingsWSDao.setApiCallbackUrl(newServerConfiguration.getRandomCallbackUrl());
			}
			return newServerConfiguration;
		} catch (Exception ex) {
			logger.error("Failed to refresh ServerConfiguration", ex);
		}
		
		return null;
	}
}
