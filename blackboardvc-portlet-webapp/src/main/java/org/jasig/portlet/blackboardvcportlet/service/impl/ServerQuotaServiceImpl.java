package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ServerQuotaDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;
import org.jasig.portlet.blackboardvcportlet.service.ServerQuotaService;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardGetServerQuotasResponseCollection;
import com.elluminate.sas.BlackboardServerQuotas;
import com.elluminate.sas.BlackboardServerQuotasResponse;
import com.elluminate.sas.ObjectFactory;

/**
 * Service class for Server Quota
 * @author rgood
 */
@Service
public class ServerQuotaServiceImpl implements ServerQuotaService
{
	private static final Logger logger = LoggerFactory.getLogger(ServerQuotaServiceImpl.class);

    private ServerQuotaDao serverQuotaDao;
	private SASWebServiceOperations sasWebServiceTemplate;

	@Autowired
	public void setServerQuotaDao(ServerQuotaDao serverQuotaDao)
	{
		this.serverQuotaDao = serverQuotaDao;
	}

	@Autowired
	public void setSasWebServiceTemplate(SASWebServiceOperations sasWebServiceTemplate)
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
		try
		{
			// Call Web Service Operation
			final JAXBElement<BlackboardServerQuotas> request = new ObjectFactory().createGetServerQuotas(null);
			BlackboardGetServerQuotasResponseCollection serverQuotasResponseCollection = (BlackboardGetServerQuotasResponseCollection)sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/GetServerQuotas", request);
			List<BlackboardServerQuotasResponse> quotaResult = serverQuotasResponseCollection.getServerQuotasResponses();
			logger.debug("Result = " + quotaResult);
			for (BlackboardServerQuotasResponse response : quotaResult) {
			    return this.serverQuotaDao.createOrUpdateQuota(response);
			}
		}
		catch (Exception ex)
		{
		    logger.error("Failed to refresh ServerQuota", ex);
		}
		
		return null;
	}
}