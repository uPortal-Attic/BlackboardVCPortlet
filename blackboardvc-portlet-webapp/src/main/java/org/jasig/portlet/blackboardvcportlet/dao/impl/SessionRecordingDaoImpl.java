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
package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jasig.jpa.BaseJpaDao;
import org.jasig.jpa.OpenEntityManager;
import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.google.common.base.Function;

@Repository
public class SessionRecordingDaoImpl extends BaseJpaDao implements SessionRecordingDao {
    
	private InternalSessionDao sessionDao;

    private CriteriaQuery<SessionRecordingImpl> findAllSessionRecordings;

    @Autowired
    public void setSessionDao(InternalSessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.findAllSessionRecordings = this.createCriteriaQuery(new Function<CriteriaBuilder, CriteriaQuery<SessionRecordingImpl>>() {
            @Override
            public CriteriaQuery<SessionRecordingImpl> apply(CriteriaBuilder cb) {
                final CriteriaQuery<SessionRecordingImpl> criteriaQuery = cb.createQuery(SessionRecordingImpl.class);
                final Root<SessionRecordingImpl> definitionRoot = criteriaQuery.from(SessionRecordingImpl.class);
                criteriaQuery.select(definitionRoot);

                return criteriaQuery;
            }
        });
    }

    @Override
    public Set<SessionRecording> getAllRecordings() {
        final TypedQuery<SessionRecordingImpl> query = this.createCachedQuery(this.findAllSessionRecordings);
        
        final List<SessionRecordingImpl> sessionRecordings = query.getResultList();
        return new LinkedHashSet<SessionRecording>(sessionRecordings);
    }
    
    @Override
    public SessionRecordingImpl getSessionRecording(long recordingId) {
        return this.getEntityManager().find(SessionRecordingImpl.class, recordingId);
    }

    @Override
    @Transactional
    public SessionRecordingImpl createOrUpdateRecording(BlackboardRecordingLongResponse recordingLongResponse) {
        final Long bbSessionId = recordingLongResponse.getSessionId();
        final SessionImpl session = this.sessionDao.getSessionByBlackboardId(bbSessionId);
        if (session == null) {
            throw new IllegalArgumentException("No session with blackboard session id '" + bbSessionId + "' exists, cannot update recording");
        }
        
        final long bbRecordingId = recordingLongResponse.getRecordingId();
        SessionRecordingImpl recording = this.getRecordingByBlackboardId(bbRecordingId);
        if (recording == null) {
            recording = new SessionRecordingImpl(bbRecordingId, session);
            logger.debug("Inserting new Recording for recording ID: " + bbRecordingId + " bbSessionId: " + session.getBbSessionId());
        }
        
        recording.setCreationDate(DaoUtils.toDateTime(recordingLongResponse.getCreationDate()));
        recording.setRecordingSize(recordingLongResponse.getRecordingSize());
        recording.setRecordingUrl(recordingLongResponse.getRecordingURL());
        recording.setRoomEnd(DaoUtils.toDateTime(recordingLongResponse.getRoomEndDate()));
        recording.setRoomStart(DaoUtils.toDateTime(recordingLongResponse.getRoomStartDate()));
        recording.setSecureSignOn(recordingLongResponse.isSecureSignOn());
        recording.setRoomName(recordingLongResponse.getRoomName());
        
        this.getEntityManager().persist(recording);
        
        return recording;
    }
    
    @Override
    @Transactional
    public void updateSessionRecording(SessionRecording recording) {
        this.getEntityManager().persist(recording);
    }

    @OpenEntityManager
    public SessionRecordingImpl getRecordingByBlackboardId(long bbRecordingId) {
        final NaturalIdQuery<SessionRecordingImpl> query = this.createNaturalIdQuery(SessionRecordingImpl.class);
        query.using(SessionRecordingImpl_.bbRecordingId, bbRecordingId);
        
        return query.load();
    }

    @Override
    @Transactional
    public void deleteRecording(SessionRecording recording) {
        final SessionRecordingImpl sessionRecording = this.getSessionRecording(recording.getRecordingId());
        
        //Remove the reference from the session to the recording
        final SessionImpl session = sessionRecording.getSession();
        session.getSessionRecordings().remove(sessionRecording);
        
        final EntityManager entityManager = this.getEntityManager();
        entityManager.remove(sessionRecording);
        entityManager.persist(session);
    }
}
