package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import org.jasig.portlet.blackboardvcportlet.dao.ServerQuotaDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;
import org.jasig.portlet.blackboardvcportlet.service.ServerQuotaService;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.elluminate.sas.GetServerQuotasResponseCollection;
import com.elluminate.sas.ObjectFactory;
import com.elluminate.sas.ServerQuotasResponse;

/**
 * Service class for Server Quota
 * @author rgood
 */
@Service
public class ServerQuotaServiceImpl implements ServerQuotaService
{
	private static final Logger logger = LoggerFactory.getLogger(ServerQuotaServiceImpl.class);

    private ServerQuotaDao serverQuotaDao;
	private SASWebServiceTemplate sasWebServiceTemplate;

	@Autowired
	public void setServerQuotaDao(ServerQuotaDao serverQuotaDao)
	{
		this.serverQuotaDao = serverQuotaDao;
	}

	@Autowired
	public void setSasWebServiceTemplate(SASWebServiceTemplate sasWebServiceTemplate)
	{
		this.sasWebServiceTemplate = sasWebServiceTemplate;
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
    
     /**
      * Refresh the server quota, only goes to Collaborate if last update
      * was longer than an hour ago.
      */
	@Scheduled(fixedRate=3600000)
    public ServerQuota refreshServerQuota()
    {
	    final ServerQuota serverQuota = this.serverQuotaDao.getServerQuota();
	    if (serverQuota != null && serverQuota.getLastUpdated().plusHours(1).isAfterNow()) {
	        //Nothing to do, serverQuota exists and is less than 1 hour old
	        return serverQuota;
	    }
	    
		logger.info("Server Quota being refreshed");
		try
		{
			// Call Web Service Operation
			GetServerQuotasResponseCollection serverQuotasResponseCollection = (GetServerQuotasResponseCollection)sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/GetServerQuotas", new ObjectFactory().createGetServerQuotas(null));
			List<ServerQuotasResponse> quotaResult = serverQuotasResponseCollection.getServerQuotasResponses();
			logger.debug("Result = " + quotaResult);
			for (ServerQuotasResponse response : quotaResult) {
			    return this.serverQuotaDao.createOrUpdateQuota(response);
			}
		}
		catch (Exception ex)
		{
			logger.error("Error refreshing ServerQuota data: ", ex);
		}
		
		return null;
	}
}