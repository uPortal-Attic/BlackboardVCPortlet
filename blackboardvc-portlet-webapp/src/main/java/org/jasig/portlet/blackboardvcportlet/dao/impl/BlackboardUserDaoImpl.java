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
import org.jasig.portlet.blackboardvcportlet.data.BlackboardSession;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;

@Repository
public class BlackboardUserDaoImpl extends BaseJpaDao implements InternalBlackboardUserDao {
    
    @Override
    public Set<BlackboardSession> getChairedSessionsForUser(long userId) {
        final BlackboardUserImpl userImpl = this.getBlackboardUser(userId);
        if (userImpl == null) {
            return null;
        }

        //Create a copy to trigger loading of the session data
        return ImmutableSet.copyOf(userImpl.getChairedSessions());
    }

    @Override
    public Set<BlackboardSession> getNonChairedSessionsForUser(long userId) {
        final BlackboardUserImpl userImpl = this.getBlackboardUser(userId);
        if (userImpl == null) {
            return null;
        }

        //Create a copy to trigger loading of the session data
        return ImmutableSet.copyOf(userImpl.getNonChairedSessions());
    }
    
    @Override
    @Transactional
    public BlackboardUserImpl createBlackboardUser(String email, String displayName) {
        final BlackboardUserImpl user = new BlackboardUserImpl(email, displayName);
        
        this.getEntityManager().persist(user);
        
        return user;
    }
    
    @Override
    @Transactional
    public BlackboardUser updateBlackboardUser(BlackboardUser user) {
        Validate.notNull(user, "user can not be null");
        
        this.getEntityManager().persist(user);
        
        return user;
    }
    
    @Override
    @Transactional
    public void deleteBlackboardUser(BlackboardUser user) {
        Validate.notNull(user, "user can not be null");
        
        final EntityManager entityManager = this.getEntityManager();
        if (!entityManager.contains(user)) {
            user = entityManager.merge(user);
        }
        entityManager.remove(user);
    }
    
    @Override
    public BlackboardUserImpl getBlackboardUser(long userId) {
        final EntityManager entityManager = this.getEntityManager();
        return entityManager.find(BlackboardUserImpl.class, userId);
    }

    @Override
    @Transactional
    public BlackboardUserImpl getOrCreateBlackboardUser(String email) {
        final BlackboardUserImpl user = this.getBlackboardUser(email);
        if (user != null) {
            return user;
        }
        
        return this.createBlackboardUser(email, null);
    }

    @Override
    @OpenEntityManager
    public BlackboardUserImpl getBlackboardUser(String email) {
        final NaturalIdQuery<BlackboardUserImpl> query = this.createNaturalIdQuery(BlackboardUserImpl.class);
        query.using(BlackboardUserImpl_.email, email);
        
        return query.load();
    }
    
    @Override
    public Set<BlackboardUser> findAllMatchingUsers(String email, Map<String, String> attributes) {
        final CriteriaQuery<BlackboardUserImpl> criteriaQuery = createSearchQuery(email, attributes);
        
        final TypedQuery<BlackboardUserImpl> query = this.createQuery(criteriaQuery);
        final List<BlackboardUserImpl> results = query.getResultList();
        return new LinkedHashSet<BlackboardUser>(results);
    }

    private CriteriaQuery<BlackboardUserImpl> createSearchQuery(String email, Map<String, String> attributes) {
        final CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<BlackboardUserImpl> criteriaQuery = cb.createQuery(BlackboardUserImpl.class);
        final Root<BlackboardUserImpl> bbu = criteriaQuery.from(BlackboardUserImpl.class);
        
        //Fetch the attributes in one query
        bbu.fetch(BlackboardUserImpl_.attributes);

        //Use a MapJoin to filter the results by attribute
        final MapJoin<BlackboardUserImpl, String, String> attrJoin = bbu.join(BlackboardUserImpl_.attributes);
        final Predicate[] predicates = new Predicate[attributes.size() + 1];
        predicates[0] = cb.equal(bbu.get(BlackboardUserImpl_.email), email);
        int pIdx = 1;
        for (final Map.Entry<String, String> attrEntry : attributes.entrySet()) {
            predicates[pIdx] = cb.and(cb.equal(attrJoin.key(), attrEntry.getKey()), cb.equal(attrJoin.value(), attrEntry.getValue()));
            pIdx++;
        }
        
        criteriaQuery.where(cb.or(predicates));
        return criteriaQuery;
    }
}
