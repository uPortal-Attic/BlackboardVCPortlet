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
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.Validate;
import org.jasig.jpa.BaseJpaDao;
import org.jasig.jpa.OpenEntityManager;
import org.jasig.portlet.blackboardvcportlet.dao.UserSessionUrlDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.UserSessionUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;

@Repository
public class UserSessionUrlDaoImpl extends BaseJpaDao implements UserSessionUrlDao {
	private CriteriaQuery<UserSessionUrlImpl> findAllUserSessionUrl;
	private CriteriaQuery<UserSessionUrlImpl> findUserSessionUrls; 
	
	private InternalConferenceUserDao blackboardUserDao;
	
	private InternalSessionDao sessionDao;
	
	@Autowired
	public void setSessionDao(InternalSessionDao dao) {
		this.sessionDao = dao;
	}

    @Autowired
    public void setBlackboardUserDao(InternalConferenceUserDao blackboardUserDao) {
        this.blackboardUserDao = blackboardUserDao;
    }

	@Override
    public void afterPropertiesSet() throws Exception {
        this.findAllUserSessionUrl = this.createCriteriaQuery(new Function<CriteriaBuilder, CriteriaQuery<UserSessionUrlImpl>>() {
            @Override
            public CriteriaQuery<UserSessionUrlImpl> apply(CriteriaBuilder cb) {
                final CriteriaQuery<UserSessionUrlImpl> criteriaQuery = cb.createQuery(UserSessionUrlImpl.class);
                final Root<UserSessionUrlImpl> definitionRoot = criteriaQuery.from(UserSessionUrlImpl.class);
                criteriaQuery.select(definitionRoot);

                return criteriaQuery;
            }
        });
        
        
    }
	
	@Override
	public Set<UserSessionUrl> getAllUserSessionUrls() {
        final TypedQuery<UserSessionUrlImpl> query = this.createQuery(this.findAllUserSessionUrl);
        return new LinkedHashSet<UserSessionUrl>(query.getResultList());
    }
	
	@Override
	@OpenEntityManager
    public UserSessionUrlImpl getUserSessionUrlsBySessionAndUser(Session session, ConferenceUser user) {
		//fetch session
		SessionImpl sessionByBlackboardId = sessionDao.getSession(session.getSessionId());
		//fetch user
		ConferenceUserImpl confUser = blackboardUserDao.getUser(user.getUserId());
		//assert they are valid
		Validate.notNull(sessionByBlackboardId);
		Validate.notNull(confUser);
		
        final NaturalIdQuery<UserSessionUrlImpl> query = this.createNaturalIdQuery(UserSessionUrlImpl.class);
        query.using(UserSessionUrlImpl_.session, sessionByBlackboardId);
        query.using(UserSessionUrlImpl_.user, confUser);
        
        return query.load();
    }
	
	@Override
	@Transactional
	public UserSessionUrlImpl createUserSessionUrl(Session session, ConferenceUser user, String url) {
		//fetch session
		SessionImpl sessionFromDb = sessionDao.getSession(session.getSessionId());
		//fetch user
		ConferenceUserImpl userFromDb = blackboardUserDao.getUser(user.getUserId());
		//assert they are valid
		Validate.notNull(sessionFromDb);
		Validate.notNull(userFromDb);
		
		final UserSessionUrlImpl urlObject = new UserSessionUrlImpl(sessionFromDb, userFromDb, url);
		
		this.getEntityManager().persist(urlObject);
		
		return urlObject;
	}
	
	@Override
	@Transactional
	public void deleteOldSessionUrls(Session session, ConferenceUser user) {
		final EntityManager entityManager = this.getEntityManager();
		
		//fetch session
		SessionImpl sessionFromDb = sessionDao.getSession(session.getSessionId());
		//fetch user
		ConferenceUserImpl userFromDb = blackboardUserDao.getUser(user.getUserId());
		//assert they are valid
		Validate.notNull(sessionFromDb);
		Validate.notNull(userFromDb);
		
		final NaturalIdQuery<UserSessionUrlImpl> query = this.createNaturalIdQuery(UserSessionUrlImpl.class);
        query.using(UserSessionUrlImpl_.session, sessionFromDb);
        query.using(UserSessionUrlImpl_.user, userFromDb);
        
        UserSessionUrlImpl url = query.load();
        
        entityManager.remove(url);
        entityManager.flush();
	}
}
