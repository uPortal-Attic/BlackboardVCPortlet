package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.Validate;
import org.jasig.jpa.BaseJpaDao;
import org.jasig.jpa.OpenEntityManager;
import org.jasig.portlet.blackboardvcportlet.dao.MultimediaDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elluminate.sas.BlackboardMultimediaResponse;
import com.google.common.base.Function;

@Repository
public class MultimediaDaoImpl extends BaseJpaDao implements MultimediaDao {
	
	private CriteriaQuery<MultimediaImpl> findAllMultimedia;
	
	private InternalConferenceUserDao conferenceUserDao;

    @Autowired
    public void setConferenceUserDao(InternalConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }

	@Override
    public void afterPropertiesSet() throws Exception {
        this.findAllMultimedia = this.createCriteriaQuery(new Function<CriteriaBuilder, CriteriaQuery<MultimediaImpl>>() {
            @Override
            public CriteriaQuery<MultimediaImpl> apply(CriteriaBuilder cb) {
                final CriteriaQuery<MultimediaImpl> criteriaQuery = cb.createQuery(MultimediaImpl.class);
                final Root<MultimediaImpl> definitionRoot = criteriaQuery.from(MultimediaImpl.class);
                criteriaQuery.select(definitionRoot);

                return criteriaQuery;
            }
        });
    }
	
    @Override
	public Set<Multimedia> getAllMultimedia() {
        final TypedQuery<MultimediaImpl> query = this.createQuery(this.findAllMultimedia);
        return new LinkedHashSet<Multimedia>(query.getResultList());
    }
    
	@Override
    public Multimedia getMultimedia(long multimediaId) {
        return this.getEntityManager().find(MultimediaImpl.class, multimediaId);
    }

    @Override
	@OpenEntityManager
    public MultimediaImpl getMultimediaByBlackboardId(long bbMultimediaId) {
        final NaturalIdQuery<MultimediaImpl> query = this.createNaturalIdQuery(MultimediaImpl.class);
        query.using(MultimediaImpl_.bbMultimediaId, bbMultimediaId);
        
        return query.load();
    }
	
    @Override
	@Transactional
    public MultimediaImpl createMultimedia(BlackboardMultimediaResponse multimediaResponse, String filename) {
        //Find the creator user
        final String creatorId = multimediaResponse.getCreatorId();
        ConferenceUserImpl creator = this.conferenceUserDao.getUserByUniqueId(creatorId);
        if (creator == null) {
            logger.warn("Internal user {} doesn't exist for {}. Creating a bare bones user to compensate", creatorId, multimediaResponse);
            creator = this.conferenceUserDao.createInternalUser(creatorId);
        }
        
        //Create and populate a new blackboardMultimedia
        final MultimediaImpl blackboardMultimedia = new MultimediaImpl(multimediaResponse.getMultimediaId(), creator);
        updateBlackboardMultimedia(multimediaResponse, filename, blackboardMultimedia);

        //Persist and return the new multimedia
        this.getEntityManager().persist(blackboardMultimedia);
        
        creator.getMultimedias().add(blackboardMultimedia);
        this.getEntityManager().persist(creator);
        
        return blackboardMultimedia;
    }
	
    @Override
	@Transactional
    public void deleteMultimedia(Multimedia multimedia) {
        Validate.notNull(multimedia, "multimedia can not be null");
        
        final EntityManager entityManager = this.getEntityManager();
        
        final MultimediaImpl multimediaImpl = entityManager.find(MultimediaImpl.class, multimedia.getMultimediaId());
        
        final ConferenceUser creator = multimediaImpl.getCreator();
        final ConferenceUserImpl creatorImpl = this.conferenceUserDao.getUser(creator.getUserId());
        creatorImpl.getMultimedias().remove(multimediaImpl);
        
        entityManager.remove(multimediaImpl);
        entityManager.remove(creatorImpl);      
    }

    private void updateBlackboardMultimedia(BlackboardMultimediaResponse multimediaResponse, String filename, MultimediaImpl multimedia) {
    	multimedia.setDescription(multimediaResponse.getDescription());
    	multimedia.setFilename(filename);
    	multimedia.setSize(multimediaResponse.getSize());
    }
}
