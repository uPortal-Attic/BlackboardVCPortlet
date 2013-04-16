package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.Validate;
import org.jasig.jpa.BaseJpaDao;
import org.jasig.jpa.OpenEntityManager;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;

@Repository
public class ConferenceUserDaoImpl extends BaseJpaDao implements InternalConferenceUserDao {

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
        return ImmutableSet.copyOf(userImpl.getOwnedSessions());
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
        return ImmutableSet.copyOf(userImpl.getChairedSessions());
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
        return ImmutableSet.copyOf(userImpl.getNonChairedSessions());
    }
    
    @Override
    @Transactional
    public ConferenceUserImpl createUser(String email, String displayName) {
        final ConferenceUserImpl user = new ConferenceUserImpl(email, displayName);
        
        this.getEntityManager().persist(user);
        
        return user;
    }
    
    @Override
    public Set<Multimedia> getMultimediasForUser(ConferenceUser user) {
    	if(user == null) {
    		return null;
    	}
    	
    	final ConferenceUserImpl userImpl = this.getUser(user.getUserId());
    	return ImmutableSet.copyOf(userImpl.getMultimedias());
    }
    
    @Override
    public Set<Presentation> getPresentationsForUser(ConferenceUser user) {
    	if(user == null) {
    		return null;
    	}
    	
    	final ConferenceUserImpl userImpl = this.getUser(user.getUserId());
    	return ImmutableSet.copyOf(userImpl.getPresentations());
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
    @Transactional
    public ConferenceUserImpl getOrCreateUser(String email) {
        final ConferenceUserImpl user = this.getUser(email);
        if (user != null) {
            return user;
        }
        
        return this.createUser(email, null);
    }

    @Override
    @OpenEntityManager
    public ConferenceUserImpl getUser(String email) {
        final NaturalIdQuery<ConferenceUserImpl> query = this.createNaturalIdQuery(ConferenceUserImpl.class);
        query.using(ConferenceUserImpl_.email, email);
        
        return query.load();
    }
    
    @Override
    public Set<ConferenceUser> findAllMatchingUsers(String email, Map<String, String> attributes) {
        final CriteriaQuery<ConferenceUserImpl> criteriaQuery = createSearchQuery(email, attributes);
        
        final TypedQuery<ConferenceUserImpl> query = this.createQuery(criteriaQuery);
        final List<ConferenceUserImpl> results = query.getResultList();
        return new LinkedHashSet<ConferenceUser>(results);
    }

    private CriteriaQuery<ConferenceUserImpl> createSearchQuery(String email, Map<String, String> attributes) {
        final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<ConferenceUserImpl> criteriaQuery = cb.createQuery(ConferenceUserImpl.class);
        final Root<ConferenceUserImpl> bbu = criteriaQuery.from(ConferenceUserImpl.class);
        
        //Fetch the attributes in one query
        bbu.fetch(ConferenceUserImpl_.attributes);

        //Use a MapJoin to filter the results by attribute
        final MapJoin<ConferenceUserImpl, String, String> attrJoin = bbu.join(ConferenceUserImpl_.attributes);
        final Predicate[] predicates = new Predicate[attributes.size() + 1];
        predicates[0] = cb.equal(bbu.get(ConferenceUserImpl_.email), email);
        int pIdx = 1;
        for (final Map.Entry<String, String> attrEntry : attributes.entrySet()) {
            predicates[pIdx] = cb.and(cb.equal(attrJoin.key(), attrEntry.getKey()), cb.equal(attrJoin.value(), attrEntry.getValue()));
            pIdx++;
        }
        
        criteriaQuery.where(cb.or(predicates));
        return criteriaQuery;
    }
}
