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
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elluminate.sas.BlackboardPresentationResponse;
import com.google.common.base.Function;

@Repository
public class PresentationDaoImpl extends BaseJpaDao implements InternalPresentationDao {
	
	private CriteriaQuery<PresentationImpl> findAllPresentation;
	
	private InternalConferenceUserDao conferenceUserDao;
	
	@Autowired
    public void setConferenceUserDao(InternalConferenceUserDao conferenceUserDao) {
        this.conferenceUserDao = conferenceUserDao;
    }
	
	@Override
    public void afterPropertiesSet() throws Exception {
        this.findAllPresentation = this.createCriteriaQuery(new Function<CriteriaBuilder, CriteriaQuery<PresentationImpl>>() {
            @Override
            public CriteriaQuery<PresentationImpl> apply(CriteriaBuilder cb) {
                final CriteriaQuery<PresentationImpl> criteriaQuery = cb.createQuery(PresentationImpl.class);
                final Root<PresentationImpl> definitionRoot = criteriaQuery.from(PresentationImpl.class);
                criteriaQuery.select(definitionRoot);

                return criteriaQuery;
            }
        });
    }

	@Override
	public Set<Presentation> getAllPresentations() {
		final TypedQuery<PresentationImpl> query = this.createQuery(this.findAllPresentation);
        return new LinkedHashSet<Presentation>(query.getResultList());
	}
	
	@Override
    public PresentationImpl getPresentationById(long presentationId) {
        return this.getEntityManager().find(PresentationImpl.class, presentationId);
    }

    @Override
	@OpenEntityManager
	public PresentationImpl getPresentationByBlackboardId(long bbPresentationId) {
		final NaturalIdQuery<PresentationImpl> query = this.createNaturalIdQuery(PresentationImpl.class);
        query.using(PresentationImpl_.bbPresentationId, bbPresentationId);
        
        return query.load();
	}

	@Override
	@Transactional
	public Presentation createPresentation(BlackboardPresentationResponse presentationResponse, String filename) {
		//Find the creator user
        final String creatorId = presentationResponse.getCreatorId();
        ConferenceUserImpl creator = this.conferenceUserDao.getUserByUniqueId(creatorId);
        if (creator == null) {
            logger.warn("Internal user {} doesn't exist for {}. Creating a bare bones user to compensate", creatorId, presentationResponse);
            creator = this.conferenceUserDao.createInternalUser(creatorId);
        }
        
        //Create and populate a new presentation
        final PresentationImpl bbPresentation = new PresentationImpl(presentationResponse.getPresentationId(), creator);
        updateBlackboardPresentation(presentationResponse, filename, bbPresentation);
        
        //Persist and return the new presentation
        this.getEntityManager().persist(bbPresentation);
        
        creator.getPresentations().add(bbPresentation);
        this.getEntityManager().persist(creator);
        
        return bbPresentation;
	}

	@Override
	@Transactional
	public void deletePresentation(Presentation presentation) {
		Validate.notNull(presentation, "presentation can not be null");
        
        final EntityManager entityManager = this.getEntityManager();
        
        final PresentationImpl presentationImpl = entityManager.find(PresentationImpl.class, presentation.getPresentationId());
        
        final ConferenceUser creator = presentationImpl.getCreator();
        final ConferenceUserImpl creatorImpl = this.conferenceUserDao.getUser(creator.getUserId());
        creatorImpl.getPresentations().remove(presentationImpl);
        
        entityManager.remove(presentationImpl);
        entityManager.persist(creatorImpl);
	}
	
	private void updateBlackboardPresentation(BlackboardPresentationResponse presentationResponse, String filename, PresentationImpl presentation) {
		presentation.setDescription(presentationResponse.getDescription());
		presentation.setFilename(filename);
		presentation.setSize(presentationResponse.getSize());
    }

}
