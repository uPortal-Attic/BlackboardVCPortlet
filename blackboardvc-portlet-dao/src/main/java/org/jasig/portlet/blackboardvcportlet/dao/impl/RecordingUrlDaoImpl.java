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
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.jasig.portlet.blackboardvcportlet.dao.RecordingUrlDao;
import org.jasig.portlet.blackboardvcportlet.data.RecordingUrl;
import org.jasig.portlet.blackboardvcportlet.data.RecordingUrlId;

/**
 * Implementation of the RecordingUrlDao interface. Allows storage, deletion
 * and retrieval of RecordingUrl
 * @author Richard Good
 */
@Transactional
@Scope("singleton")
@Component("recordingUrlDao")
public class RecordingUrlDaoImpl extends HibernateDaoSupport implements RecordingUrlDao {
    
    /**
     * Gets all recording urls for a recordingId.
     * @param recordingId The recordingId to retrieve urls for
     * @return List<RecordingUrl>
     */
    @Override
    public List<RecordingUrl>getRecordingUrls(Long recordingId)
    {
        Criteria criteria = this.getSession().createCriteria(RecordingUrl.class).add(Restrictions.eq("recordingUrlId.recordingId",recordingId));
        
        return (List<RecordingUrl>)criteria.list();
    }
    
    /**
     * Stores a RecordingUrl
     * @param recordingUrl The RecordingUrl to store.
     */
    @Override
    public void saveRecordingUrl(RecordingUrl recordingUrl)
    {
        this.getHibernateTemplate().saveOrUpdate(recordingUrl);
    }
    
    /**
     * Deletes a RecordingUrl
     * @param recordingUrlId The id of the recording URL to delete
     */
    @Override
    public void deleteRecordingUrl(RecordingUrlId recordingUrlId)
    {
        this.getHibernateTemplate().delete(this.getHibernateTemplate().get(RecordingUrl.class,recordingUrlId));
    }
    
    /**
     * Deletes all RecordingUrl for a recordingId.
     * @param recordingId The Id to delete all RecordingUrl from
     */
    @Override
    public void deleteRecordingUrls(Long recordingId)
    {
        Criteria criteria = this.getSession().createCriteria(RecordingUrl.class).add(Restrictions.eq("recordingUrlId.recordingId",recordingId));
        this.getHibernateTemplate().deleteAll(criteria.list());
    }
      
}
