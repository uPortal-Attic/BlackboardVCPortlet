package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.Validate;
import org.jasig.jpa.BaseJpaDao;
import org.jasig.jpa.OpenEntityManager;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

@Repository
public class ConferenceUserDaoImpl extends BaseJpaDao implements InternalConferenceUserDao {
    private ParameterExpression<String> emailParameter;
    
    private CriteriaQuery<ConferenceUserImpl> getUsersByPrimaryEmailQuery;
    private CriteriaQuery<ConferenceUserImpl> getUsersByAnyEmailQuery;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.emailParameter = this.createParameterExpression(String.class, "email");
        
        this.getUsersByPrimaryEmailQuery = this.createCriteriaQuery(new Function<CriteriaBuilder, CriteriaQuery<ConferenceUserImpl>>() {
            @Override
            public CriteriaQuery<ConferenceUserImpl> apply(CriteriaBuilder cb) {
                final CriteriaQuery<ConferenceUserImpl> criteriaQuery = cb.createQuery(ConferenceUserImpl.class);
                final Root<ConferenceUserImpl> definitionRoot = criteriaQuery.from(ConferenceUserImpl.class);
                criteriaQuery.select(definitionRoot);
                criteriaQuery.where(cb.equal(definitionRoot.get(ConferenceUserImpl_.email), emailParameter));

                return criteriaQuery;
            }
        });

        this.getUsersByAnyEmailQuery = this.createCriteriaQuery(new Function<CriteriaBuilder, CriteriaQuery<ConferenceUserImpl>>() {
            @Override
            public CriteriaQuery<ConferenceUserImpl> apply(CriteriaBuilder cb) {
                final CriteriaQuery<ConferenceUserImpl> criteriaQuery = cb.createQuery(ConferenceUserImpl.class);
                final Root<ConferenceUserImpl> definitionRoot = criteriaQuery.from(ConferenceUserImpl.class);
                criteriaQuery.select(definitionRoot);
                criteriaQuery.where(cb.or(
                        cb.equal(definitionRoot.get(ConferenceUserImpl_.email), emailParameter),
                        cb.isMember(emailParameter, definitionRoot.get(ConferenceUserImpl_.additionalEmails))
                    ));

                return criteriaQuery;
            }
        });
    }
    
    @Override
    public Set<Session> getOwnedSessionsForUser(ConferenceUser user) {
        if (user == null) {
            return null;
        }
        
        final ConferenceUserImpl userImpl = this.getUser(user.getUserId());
        if (userImpl == null) {
            return null;
        }

        //Create a copy to trigger loading of the session data
        return ImmutableSet.<Session>copyOf(userImpl.getOwnedSessions());
    }
    @Override
    public Set<Session> getChairedSessionsForUser(ConferenceUser user) {
        if (user == null) {
            return null;
        }
        
        final ConferenceUserImpl userImpl = this.getUser(user.getUserId());
        if (userImpl == null) {
            return null;
        }

        //Create a copy to trigger loading of the session data
        return ImmutableSet.<Session>copyOf(userImpl.getChairedSessions());
    }

    @Override
    public Set<Session> getNonChairedSessionsForUser(ConferenceUser user) {
        if (user == null) {
            return null;
        }
        
        final ConferenceUserImpl userImpl = this.getUser(user.getUserId());
        if (userImpl == null) {
            return null;
        }

        //Create a copy to trigger loading of the session data
        return ImmutableSet.<Session>copyOf(userImpl.getNonChairedSessions());
    }
    
    @Override
    @Transactional
    public ConferenceUserImpl createInternalUser(String uniqueId) {
        final ConferenceUserImpl user = new ConferenceUserImpl(uniqueId);
        this.getEntityManager().persist(user);
        
        return user;
    }
    
    
    @Override
    @Transactional
    public ConferenceUserImpl createExternalUser(String displayName, String email) {
        final String invitationToken = RandomStringUtils.randomAlphanumeric(20);
        final ConferenceUserImpl user = new ConferenceUserImpl(email, invitationToken);
        user.setDisplayName(displayName);
        
        this.getEntityManager().persist(user);
        
        return user;
    }
    
    @Override
    @Transactional
    public ConferenceUserImpl createExternalUser(String email) {
        return this.createExternalUser(null, email);
    }
    
    @Override
    public Set<Multimedia> getMultimediasForUser(ConferenceUser user) {
    	if(user == null) {
    		return null;
    	}
    	
    	final ConferenceUserImpl userImpl = this.getUser(user.getUserId());
    	return ImmutableSet.<Multimedia>copyOf(userImpl.getMultimedias());
    }
    
    @Override
    public Set<Presentation> getPresentationsForUser(ConferenceUser user) {
    	if(user == null) {
    		return null;
    	}
    	
    	final ConferenceUserImpl userImpl = this.getUser(user.getUserId());
    	return ImmutableSet.<Presentation>copyOf(userImpl.getPresentations());
    }
    
    @Override
    @Transactional
    public ConferenceUser updateUser(ConferenceUser user) {
        Validate.notNull(user, "user can not be null");
        
        this.getEntityManager().persist(user);
        
        return user;
    }
    
    @Override
    @Transactional
    public void deleteUser(ConferenceUser user) {
        Validate.notNull(user, "user can not be null");
        
        final EntityManager entityManager = this.getEntityManager();
        if (!entityManager.contains(user)) {
            user = entityManager.merge(user);
        }
        entityManager.remove(user);
    }
    
    @Override
    public ConferenceUserImpl getUser(long userId) {
        final EntityManager entityManager = this.getEntityManager();
        return entityManager.find(ConferenceUserImpl.class, userId);
    }

    @Override
    @OpenEntityManager
    public ConferenceUserImpl getUserByUniqueId(String uniqueId) {
        final NaturalIdQuery<ConferenceUserImpl> query = this.createNaturalIdQuery(ConferenceUserImpl.class);
        query.using(ConferenceUserImpl_.uniqueId, uniqueId);
        query.using(ConferenceUserImpl_.external, false);
        
        return query.load();
    }
    
    @Override
    @OpenEntityManager
    public ConferenceUserImpl getExternalUserByEmail(String email) {
        final NaturalIdQuery<ConferenceUserImpl> query = this.createNaturalIdQuery(ConferenceUserImpl.class);
        query.using(ConferenceUserImpl_.uniqueId, email);
        query.using(ConferenceUserImpl_.external, true);
        
        return query.load();
    }
    
    @Override
    public Set<ConferenceUser> getUsersByAnyEmail(String email) {
        final TypedQuery<ConferenceUserImpl> query = this.createQuery(this.getUsersByAnyEmailQuery);
        query.setParameter(this.emailParameter, email);
        
        final List<ConferenceUserImpl> resultList = query.getResultList();
        return new LinkedHashSet<ConferenceUser>(resultList);
    }
    
    @Override
    public Set<ConferenceUser> getUsersByPrimaryEmail(String email) {
        final TypedQuery<ConferenceUserImpl> query = this.createQuery(this.getUsersByPrimaryEmailQuery);
        query.setParameter(this.emailParameter, email);
        
        final List<ConferenceUserImpl> resultList = query.getResultList();
        return new LinkedHashSet<ConferenceUser>(resultList);
    }
}
