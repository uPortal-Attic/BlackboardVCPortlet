package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

@Repository
public class SessionRecordingDaoImpl extends BaseJpaDao implements SessionRecordingDao {
    
	private InternalSessionDao sessionDao;
    

    private ParameterExpression<Long> recordingIdParameter;
    
    private String deleteRecordingsByIdQueryString;
    private CriteriaQuery<SessionRecordingImpl> findAllSessionRecordings;

    @Autowired
    public void setSessionDao(InternalSessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.recordingIdParameter = this.createParameterExpression(Long.class, "id");
        
        this.deleteRecordingsByIdQueryString = 
                "DELETE FROM " + SessionRecordingImpl.class.getName() + " e " +
                "WHERE e." + SessionRecordingImpl_.recordingId.getName() + " in :" + this.recordingIdParameter.getName();
        
        
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
    public SessionRecording getSessionRecording(long recordingId) {
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
    public int deleteRecordings(long... recordingIds) {
        final Query deleteRecordingsQuery = this.getEntityManager().createQuery(this.deleteRecordingsByIdQueryString);
        
        final Builder<Long> idSetBuilder = ImmutableSet.builder();
        for (final long recordingId : recordingIds) {
            idSetBuilder.add(recordingId);
        }
        
        final ImmutableSet<Long> idSet = idSetBuilder.build();
        deleteRecordingsQuery.setParameter(this.recordingIdParameter.getName(), idSet);
        final int deletedRecordings = deleteRecordingsQuery.executeUpdate();
        
        logger.debug("Deleted {} recordings for ids: {}", deletedRecordings, idSet);
        
        return deletedRecordings;
    }
}
