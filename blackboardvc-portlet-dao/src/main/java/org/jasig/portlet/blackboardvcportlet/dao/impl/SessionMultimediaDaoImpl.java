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
import org.jasig.portlet.blackboardvcportlet.dao.SessionMultimediaDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionMultimedia;
import org.jasig.portlet.blackboardvcportlet.data.SessionMultimediaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementation of the SessionMultimediaDao interface. Allows the storage,
 * deletion and retrieval of SessionMultimedia
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("sessionMultimediaDao")
public class SessionMultimediaDaoImpl extends HibernateDaoSupport implements SessionMultimediaDao{

	@Autowired
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	/**
     * Deletes a particular SessionMultimedia
     * @param multimediaId The Id of the SessionMultimedia to delete
     */
    @Override
    public void deleteSessionMultimedia(long multimediaId) {
        this.getHibernateTemplate().delete(this.getHibernateTemplate().get(SessionMultimediaImpl.class,multimediaId));
    }

    /**
     * Gets SessionMultimedia for a particular Session
     * @param sessionId The session ID to retrieve SessionMultimedia for
     * @return List<SessionMultimedia>
     */
    @Override
    public List<SessionMultimedia> getSessionMultimedia(String sessionId) {
        Criteria criteria = this.getSession().createCriteria(SessionMultimediaImpl.class).add(Restrictions.eq("sessionId",sessionId));
        return criteria.list();
    }

    /**
     * Stores a SessionMultimedia
     * @param sessionMultimedia The SessionMultimedia to store
     */
    @Override
    public void saveSessionMultimedia(SessionMultimedia sessionMultimedia) {
        this.getHibernateTemplate().saveOrUpdate(sessionMultimedia);
    }
    
}
