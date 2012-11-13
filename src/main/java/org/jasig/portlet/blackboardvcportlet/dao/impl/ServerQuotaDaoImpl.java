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
package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.portlet.blackboardvcportlet.dao.ServerQuotaDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;

/**
 * Implementation of ServerQuotaDao interface, allows the storage, deletion
 * and retrieval of ServerQuota
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("serverQuotaDao")
public class ServerQuotaDaoImpl extends HibernateDaoSupport implements ServerQuotaDao {
    
    /**
     * Gets the ServerQuota
     * @return ServerQuota
     */
    @Override
    public ServerQuota getServerQuota(){
        
        Criteria criteria = this.getSession().createCriteria(ServerQuota.class);
        List<ServerQuota> quotaList = criteria.list();
        if (quotaList!=null&&quotaList.size()>0)
        {
           return (ServerQuota)criteria.list().get(0); 
        }
        else 
        {
            return null;
        }
        
    }
	
    /**
     * Stores the ServerQuota
     * @param serverQuota The ServerQuota to store.
     */
    @Override
    public void saveServerQuota(ServerQuota serverQuota){
        this.logger.debug("called saveServerQuota:"+serverQuota);
        this.getHibernateTemplate().saveOrUpdate(serverQuota);   
    }
	
    /**
     * Deletes the stored ServerQuota
     */
    @Override
    public void deleteServerQuota(){
        Criteria criteria = this.getSession().createCriteria(ServerQuota.class);
        this.getHibernateTemplate().deleteAll(criteria.list());
    }
    
}
