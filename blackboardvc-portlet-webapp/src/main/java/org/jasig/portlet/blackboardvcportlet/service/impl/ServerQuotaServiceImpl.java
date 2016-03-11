/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
    
    @Scheduled(fixedRate=86400000)
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
	    if (serverQuota != null && serverQuota.getLastUpdated().plusWeeks(1).isAfterNow()) {
	        //Nothing to do, serverQuota exists and is less than 1 week old
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