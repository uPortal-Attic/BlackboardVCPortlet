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
import org.jasig.portlet.blackboardvcportlet.dao.SessionPresentationDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionPresentation;
import org.jasig.portlet.blackboardvcportlet.data.SessionPresentationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Implementation of the SessionPresentationDao interface. Supports storage
 * deletion and retrieval of session presentations
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("sessionPresentationDao")
public class SessionPresentationDaoImpl extends HibernateDaoSupport implements SessionPresentationDao {

	@Autowired
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	/**
     * Deletes a SessionPresentation
     * @param presentationId The ID of the SessionPresentation to delete
     */
    @Override
    public void deleteSessionPresentation(String presentationId) {
        this.getHibernateTemplate().delete(this.getHibernateTemplate().get(SessionPresentationImpl.class,Long.valueOf(presentationId)));
    }

    /**
     * Stores a SessionPresentation
     * @param sessionPresentation The SessionPresentation to delete
     */
    @Override
    public void storeSessionPresentation(SessionPresentation sessionPresentation) {
        this.getHibernateTemplate().saveOrUpdate(sessionPresentation);
    }

    /**
     * Gets the SessionPresentation for a session
     * @param sessionId The Id of the session to retrieve the presentation from
     * @return List<SessionPresentation>
     */
    @Override
    public List<SessionPresentation> getSessionPresentation(String sessionId) {
        Criteria criteria = this.getSession().createCriteria(SessionPresentationImpl.class).add(Restrictions.eq("sessionId",sessionId));
        return criteria.list();
    }
    
}
