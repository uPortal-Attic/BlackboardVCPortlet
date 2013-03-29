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
import org.jasig.portlet.blackboardvcportlet.dao.SessionExtParticipantDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionExtParticipant;
import org.jasig.portlet.blackboardvcportlet.data.SessionExtParticipantImpl;
import org.jasig.portlet.blackboardvcportlet.data.SessionExtParticipantId;

/**
 * Implementation of the SessionExtParticipantDao interface. Allows the deletion
 * storage and retrieval of SessionExtParticipants
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("sessionExtParticipant")
public class SessionExtParticipantDaoImpl extends HibernateDaoSupport implements SessionExtParticipantDao {

	@Autowired
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	/**
     * Stores a SessionExtParticipant
     * @param sessionExtParticipant The SessionExtParticipant to store
     */
    @Override
    public void storeSessionExtParticipant(SessionExtParticipant sessionExtParticipant) {
        this.getHibernateTemplate().saveOrUpdate(sessionExtParticipant);
    }

    /**
     * Gets a SessionExtParticipant
     * @param sessionExtParticipantId The Id of the SessionExtParticipant to retrieve
     * @return SessionExtParticipant
     */
    @Override
    public SessionExtParticipant getSessionExtParticipant(SessionExtParticipantId sessionExtParticipantId) {
        return (SessionExtParticipant)this.getHibernateTemplate().get(SessionExtParticipantImpl.class, sessionExtParticipantId);
    }

    /**
     * Deletes a SessionExtParticipant
     * @param sessionExtParticipant The SessionExtParticipant to delete
     */
    @Override
    public void deleteSessionextParticipant(SessionExtParticipant sessionExtParticipant) {
        this.getHibernateTemplate().delete(sessionExtParticipant);
    }

    /**
     * Deletes all SessionExtParticipant for a session
     * @param sessionId The session from which to delete all SessionExtParticipant
     */
    @Override
    public void deleteAllExtParticipants(long sessionId) {
        Criteria criteria = this.getSession().createCriteria(SessionExtParticipantImpl.class).add(Restrictions.eq("sessionExtParticipantId.sessionId",sessionId));
        this.getHibernateTemplate().deleteAll(criteria.list());
    }
    
}
