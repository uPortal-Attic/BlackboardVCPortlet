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
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.data.RecordingShortImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of RecordingShortDao interface. Allows storage, deletion
 * and retrieval of RecordingShort.
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("recordingShortDao")
public class RecordingShortDaoImpl extends HibernateDaoSupport implements SessionRecordingDao {

	@Autowired
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

    /**
     * Deletes a recordingShort
     * @param recordingId The recording ID to delete
     */
    @Override
    public void deleteRecordingShort(long recordingId)
    {
        this.getHibernateTemplate().delete(this.getHibernateTemplate().get(RecordingShortImpl.class,recordingId));
    }
    
    /**
     * Stores a RecordignShort
     * @param recordingShort The recording to store
     */
    @Override
    public void saveRecordingShort(SessionRecording recordingShort)
    {
        this.getHibernateTemplate().saveOrUpdate(recordingShort);
    }
    
    /**
     * Deletes all RecordingShort associated with a session
     * @param sessionId The session to remove RecordingShort from
     */
    @Override
    public void deleteAllRecordingShort(long sessionId)
    {
        this.getHibernateTemplate().deleteAll(this.getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(RecordingShortImpl.class).add(Restrictions.eq("sessionId",sessionId)).list());                
    }
    
    /**
     * Get all recordings
     * @return List<RecordingShort>
     */
    @Override
    public List<SessionRecording> getAllRecordings()
    {
         Criteria criteria =this.getSession().createCriteria(RecordingShortImpl.class);
         criteria.addOrder(Order.desc("creationDate"));
         return criteria.list();
    }

    /**
     * Gets all recordings which are associated with a session where the user
     * is chair or non-chair.
     * @param uid The uid to check for
     * @return List<RecordingShort>
     */
    @Override
    public List<SessionRecording> getRecordingsForUser(String uid) {
        List<SessionRecording> recordings;
        Criteria criteria = this.getSession().createCriteria(RecordingShortImpl.class);
        Criterion chairCriteria = Restrictions.ilike("chairList", uid, MatchMode.ANYWHERE);
        Criterion nonChairCriteria = Restrictions.ilike("nonChairList", uid, MatchMode.ANYWHERE);      
        Criterion chairEndCriteria = Restrictions.ilike("chairList", uid,MatchMode.END);     
        Criterion nonChairEndCriteria = Restrictions.ilike("nonChairList", uid,MatchMode.END);           
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(chairCriteria);
        disjunction.add(chairEndCriteria);
        disjunction.add(nonChairCriteria);
        disjunction.add(nonChairEndCriteria);     
        criteria.add(disjunction);
        criteria.addOrder(Order.desc("creationDate"));
        
        recordings = criteria.list();
        if (recordings==null)
        {
            logger.debug("No sessions found, creating blank list");
            recordings = new ArrayList<SessionRecording>();
        }
       
        return recordings;
    }

    /**
     * Gets all RecordingShort for a particular session
     * @param sessionId The session ID to retrieve recordings for
     * @return List<RecordingShort>
     */
    @Override
    public List<SessionRecording> getAllSessionRecordings(long sessionId) {
        
        List<SessionRecording> recordings;
        Criteria criteria = this.getSession().createCriteria(RecordingShortImpl.class).add(Restrictions.eq("sessionId",sessionId));
        recordings = criteria.list();
        if (recordings==null)
        {
            logger.debug("No sessions found, creating blank list");
            recordings = new ArrayList<SessionRecording>();
        }
        
        return recordings;
        
    }

    @Override
    public SessionRecording getRecording(long recordingShortId) {
        return (SessionRecording)this.getHibernateTemplate().get(RecordingShortImpl.class, recordingShortId);
    }
  
}
