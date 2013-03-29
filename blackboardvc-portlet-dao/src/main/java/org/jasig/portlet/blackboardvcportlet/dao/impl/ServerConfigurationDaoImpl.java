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

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.jasig.portlet.blackboardvcportlet.dao.ServerConfigurationDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfigurationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementation of the ServerConfigurationDao interface. Allows storage, 
 * deletion and retrieval of ServerConfiguration
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("serverConfigurationDao")
public class ServerConfigurationDaoImpl extends HibernateDaoSupport implements ServerConfigurationDao{

	@Autowired
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	/**
     * Gets the ServerConfiguration
     * @return ServerConfiguration
     */
    @Override
    public ServerConfiguration getServerConfiguration()
    {
        Criteria criteria = this.getSession().createCriteria(ServerConfigurationImpl.class);
        List<ServerConfigurationImpl> configurationList = criteria.list();
        if (configurationList!=null&&configurationList.size()>0)
        {
            return (ServerConfiguration)criteria.list().get(0);
        }
        else
        {
            return null;
        }
        
    }
    
    /**
     * Stores a ServerConfiguration
     * @param serverConfiguration The ServerConfiguration to store.
     * @return int
     */
    @Override
    public int saveServerConfiguration(ServerConfiguration serverConfiguration)
    {
        return (Integer)this.getHibernateTemplate().save(serverConfiguration);
    }
    
    /**
     * Deletes the ServerConfiguration
     */
    @Override
    public void deleteServerConfiguration()
    {
        Criteria criteria = this.getSession().createCriteria(ServerConfigurationImpl.class);
        this.getHibernateTemplate().deleteAll(criteria.list());
    }
    
}
