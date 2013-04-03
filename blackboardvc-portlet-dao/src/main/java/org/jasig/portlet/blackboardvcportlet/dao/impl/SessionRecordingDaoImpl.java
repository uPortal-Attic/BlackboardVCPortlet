package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jasig.portlet.blackboardvcportlet.dao.BlackboardSessionDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elluminate.sas.ListRecordingLong;
import com.google.common.base.Function;

@Repository
public class SessionRecordingDaoImpl extends BaseJpaDao implements SessionRecordingDao {
    private BlackboardSessionDao sessionDao;
    
    private CriteriaQuery<SessionRecordingImpl> findAllSessionRecordings;

    @Autowired
    public void setSessionDao(BlackboardSessionDao sessionDao) {
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
    public SessionRecording createOrUpdateRecording(ListRecordingLong listRecordingLong) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteRecordings(int... recordingIds) {
        // TODO see JpaPortletCookieDaoImpl for batch delete example
        
    }

    
}
