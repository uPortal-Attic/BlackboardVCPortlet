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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.portlet.blackboardvcportlet.dao.SessionUrlDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionUrl;
import org.jasig.portlet.blackboardvcportlet.data.SessionUrlImpl;
import org.jasig.portlet.blackboardvcportlet.data.SessionUrlId;

/**
 * Implementation of the SessionUrlDao interface. Allows the storage, retrieval
 * and deletion of SessionUrl.
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("sessionUrlDao")
public class SessionUrlDaoImpl extends HibernateDaoSupport implements SessionUrlDao {

	@Autowired
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	/**
     * Gets a Session URL
     * @param sessionUrlId The ID of the SessionUrl to retrieve.
     * @return SessionUrl
     */
    @Override
    public SessionUrl getSessionUrl(SessionUrlId sessionUrlId)
    {
      return (SessionUrl)this.getHibernateTemplate().get(SessionUrlImpl.class, sessionUrlId);
    }
    
    /**
     * Stores a SessionUrl
     * @param sessionUrl The SessionUrl to store.
     */
    @Override
    public void saveSessionUrl(SessionUrl sessionUrl)
    {
        this.getHibernateTemplate().saveOrUpdate(sessionUrl);
    }
        
    /**
     * Deletes a SessionUrl
     * @param sessionUrlId The ID of the SessionUrl to delete
     */
    @Override
    public void deleteSessionUrl(SessionUrlId sessionUrlId)
    {
        this.getHibernateTemplate().delete(this.getHibernateTemplate().get(SessionUrlImpl.class,sessionUrlId));
    }
    
    /**
     * Deletes all SessionUrl for a particular session
     * @param sessionId The session ID to delete all SessionUrl from
     */
    @Override
    public void deleteSessionUrls(long sessionId)
    {
        Criteria criteria = this.getSession().createCriteria(SessionUrlImpl.class).add(Restrictions.eq("sessionUrlId.sessionId",sessionId));
        this.getHibernateTemplate().deleteAll(criteria.list());
    }
    
}
