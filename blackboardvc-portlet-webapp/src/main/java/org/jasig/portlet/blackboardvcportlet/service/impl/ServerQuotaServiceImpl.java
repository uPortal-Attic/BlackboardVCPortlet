package org.jasig.portlet.blackboardvcportlet.service.impl;

import org.jasig.portlet.blackboardvcportlet.dao.ServerQuotaDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.GlobalSettingsWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;
import org.jasig.portlet.blackboardvcportlet.service.ServerQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * Service class for Server Quota
 * @author rgood
 */
@Service
public class ServerQuotaServiceImpl implements ServerQuotaService
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ServerQuotaDao serverQuotaDao;
	private GlobalSettingsWSDao globalSettingsWSDao;

	@Autowired
	public void setServerQuotaDao(ServerQuotaDao serverQuotaDao)
	{
		this.serverQuotaDao = serverQuotaDao;
	}

	@Autowired
	public void setGlobalSettingWSDao(GlobalSettingsWSDao value) {
		this.globalSettingsWSDao = value;
	}

    /**
     * Gets the Server quota
     * 
     * @return ServerQuota
     */
    public ServerQuota getServerQuota() {
        ServerQuota serverQuota = this.serverQuotaDao.getServerQuota();
        if (serverQuota == null) {
            serverQuota = this.refreshServerQuota();
        }
        return serverQuota;
    }
    
    @Scheduled(fixedRate=3600000)
    public void scheduledRefreshServerQuota() {
        this.refreshServerQuota();
    }
    
     /**
      * Refresh the server quota, only goes to Collaborate if last update
      * was longer than an hour ago.
      */
	public ServerQuota refreshServerQuota()
    {
	    final ServerQuota serverQuota = this.serverQuotaDao.getServerQuota();
	    if (serverQuota != null && serverQuota.getLastUpdated().plusHours(1).isAfterNow()) {
	        //Nothing to do, serverQuota exists and is less than 1 hour old
	        return serverQuota;
	    }
	    
		logger.info("Server Quota being refreshed");
		try {
			return this.serverQuotaDao.createOrUpdateQuota(globalSettingsWSDao.getServerQuota());
		} catch (Exception ex) {
		    logger.error("Failed to refresh ServerQuota", ex);
		}
		
		return null;
	}
}