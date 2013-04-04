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
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUserImpl;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUserImpl_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BlackboardUserDaoImpl extends BaseJpaDao implements BlackboardUserDao {
    
    @Override
    @Transactional
    public BlackboardUser createBlackboardUser(String email, String displayName) {
        final BlackboardUser msg = new BlackboardUserImpl(email, displayName);
        
        this.getEntityManager().persist(msg);
        
        return msg;
    }
    
    @Override
    @Transactional
    public BlackboardUser updateBlackboardUser(BlackboardUser user) {
        Validate.notNull(user, "message can not be null");
        
        this.getEntityManager().persist(user);
        
        return user;
    }
    
    @Override
    @Transactional
    public void deleteBlackboardUser(BlackboardUser user) {
        Validate.notNull(user, "message can not be null");
        
        final BlackboardUser msg;
        final EntityManager entityManager = this.getEntityManager();
        if (entityManager.contains(user)) {
            msg = user;
        } else {
            msg = entityManager.merge(user);
        }
        entityManager.remove(msg);
    }
    
    //TODO need @OpenEntityManager
    @Override
    public BlackboardUser getBlackboardUser(String email) {
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
