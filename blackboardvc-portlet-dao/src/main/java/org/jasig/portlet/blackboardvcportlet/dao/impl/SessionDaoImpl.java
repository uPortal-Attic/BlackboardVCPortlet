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

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.data.Session;

/**
 * Implementation of the SessionDao interface, allows the storage, retrieval and 
 * deletion of Session
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("sessionDao")
public class SessionDaoImpl extends HibernateDaoSupport implements SessionDao {
    
    /**
     * Gets a session.
     * @param sessionId The ID of the session to retrieve
     * @return Session
     */
    @Override
    public Session getSession(Long sessionId)
    {
        logger.debug("getSession called for sessionId:"+sessionId);
       return (Session)this.getHibernateTemplate().get(Session.class,sessionId);
    }
    
    /**
     * Stores a session
     * @param session The session to store
     */
    @Override
    public void saveSession(Session session)
    {
        this.getHibernateTemplate().saveOrUpdate(session);
    }
    
    /**
     * Deletes a session
     * @param sessionId The session ID to delete
     */
    @Override
    public void deleteSession(Long sessionId)
    {
        this.getHibernateTemplate().delete(this.getSession(sessionId));
    }
    
    /**
     * Gets all sessions
     * @return List<Session>
     */
    @Override
    public List<Session> getAllSesssions()
    {
        logger.debug("getAllSessions called");
        Criteria criteria = this.getSession().createCriteria(Session.class).addOrder(Order.desc("startTime"));
        return criteria.list();
    }

    /**
     * Gets the sessions for a particular user, where they are the creator, or 
     * in the chair/non-chair list.
     * @param uid The uid to retrieve sessions for
     * @return List<Session>
     */
    @Override
    public List<Session> getSessionsForUser(String uid) {
        logger.debug("uid:"+uid);
        List<Session> sessions;
        Criteria criteria = this.getSession().createCriteria(Session.class);
        Criterion chairCriteria = Restrictions.ilike("chairList", uid+",", MatchMode.ANYWHERE);
        Criterion chairEndCriteria = Restrictions.ilike("chairList", uid,MatchMode.END);
        Criterion nonChairCriteria = Restrictions.ilike("nonChairList", uid+",", MatchMode.ANYWHERE);
        Criterion nonChairEndCriteria = Restrictions.ilike("nonChairList", uid,MatchMode.END);
        Criterion ownerCriteria = Restrictions.eq("creatorId", uid);
        
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(chairCriteria);
        disjunction.add(chairEndCriteria);
        disjunction.add(nonChairCriteria);
        disjunction.add(nonChairEndCriteria);
        disjunction.add(ownerCriteria);
        criteria.add(disjunction);
        criteria.addOrder(Order.desc("startTime"));
        sessions = criteria.list();
        if (sessions==null)
        {
            logger.debug("No sessions found, creating blank list");
            sessions = new ArrayList<Session>();
        }
       
        return sessions;
    }
    
    
    
}
