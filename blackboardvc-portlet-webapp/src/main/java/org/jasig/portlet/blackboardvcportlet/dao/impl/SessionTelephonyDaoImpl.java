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
import org.jasig.portlet.blackboardvcportlet.dao.SessionTelephonyDao;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elluminate.sas.BlackboardSessionTelephonyResponse;
import com.google.common.base.Function;

@Repository
public class SessionTelephonyDaoImpl extends BaseJpaDao implements SessionTelephonyDao {
	private InternalSessionDao sessionDao;

    private CriteriaQuery<SessionTelephonyImpl> findAllSessionTelephony;

    @Autowired
    public void setSessionDao(InternalSessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.findAllSessionTelephony = this.createCriteriaQuery(new Function<CriteriaBuilder, CriteriaQuery<SessionTelephonyImpl>>() {
            @Override
            public CriteriaQuery<SessionTelephonyImpl> apply(CriteriaBuilder cb) {
                final CriteriaQuery<SessionTelephonyImpl> criteriaQuery = cb.createQuery(SessionTelephonyImpl.class);
                final Root<SessionTelephonyImpl> definitionRoot = criteriaQuery.from(SessionTelephonyImpl.class);
                criteriaQuery.select(definitionRoot);

                return criteriaQuery;
            }
        });
    }

    @Override
    public Set<SessionTelephony> getAllTelephony() {
        final TypedQuery<SessionTelephonyImpl> query = this.createCachedQuery(this.findAllSessionTelephony);
        
        final List<SessionTelephonyImpl> sessionTelephonyList = query.getResultList();
        return new LinkedHashSet<SessionTelephony>(sessionTelephonyList);
    }
    
    @Override
    public SessionTelephonyImpl getSessionTelephony(long telephonyId) {
        return this.getEntityManager().find(SessionTelephonyImpl.class, telephonyId);
    }

    @Override
    @Transactional
    public SessionTelephonyImpl createOrUpdateTelephony(BlackboardSessionTelephonyResponse telephonyResponse) {
        final Long bbSessionId = telephonyResponse.getSessionId();
        final SessionImpl session = this.sessionDao.getSessionByBlackboardId(bbSessionId);
        if (session == null) {
            throw new IllegalArgumentException("No session with blackboard session id '" + bbSessionId + "' exists, cannot update recording");
        }
        
        SessionTelephonyImpl telephony = DataAccessUtils.singleResult(session.getSessionTelephony());
        if (telephony == null) {
        	telephony = new SessionTelephonyImpl(session);
        	session.getSessionTelephony().add(telephony);
        }
        
        telephony.setChairPhone(telephonyResponse.getChairPhone());
        telephony.setChairPIN(telephonyResponse.getChairPIN());
        telephony.setNonChairPhone(telephonyResponse.getNonChairPhone());
        telephony.setNonChairPIN(telephonyResponse.getNonChairPIN());
        telephony.setPhone(telephonyResponse.isIsPhone());
        telephony.setSessionPIN(telephonyResponse.getSessionPIN());
        telephony.setSessionSIPPhone(telephonyResponse.getSessionSIPPhone());
        
        this.getEntityManager().persist(telephony);
        this.getEntityManager().persist(session);
        
        return telephony;
    }
    
    @Override
    @Transactional
    public void updateSessionTelephony(SessionTelephony telephony) {
        this.getEntityManager().persist(telephony);
    }

    @Override
    @Transactional
    public void deleteTelephony(long sessionId) {
    	final SessionImpl session = sessionDao.getSession(sessionId);
    	final SessionTelephonyImpl sessionTelephony = DataAccessUtils.singleResult(session.getSessionTelephony());
        
        //Remove the reference from the session to the recording
        
        session.getSessionTelephony().remove(sessionTelephony);
        
        final EntityManager entityManager = this.getEntityManager();
        entityManager.remove(sessionTelephony);
        entityManager.persist(session);
    }

	@Override
	@Transactional
	public SessionTelephony getSessionTelephony(Session session) {
		return DataAccessUtils.singleResult(sessionDao.getSessionTelephony(session));
		
	}
}
