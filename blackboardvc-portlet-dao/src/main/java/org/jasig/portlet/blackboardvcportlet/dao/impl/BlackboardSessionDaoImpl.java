package org.jasig.portlet.blackboardvcportlet.dao.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.apache.commons.lang.Validate;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardSession;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elluminate.sas.SessionResponse;
import com.google.common.collect.ImmutableSet;

@Repository
public class BlackboardSessionDaoImpl extends BaseJpaDao implements InternalBlackboardSessionDao {
    private static final Pattern USER_LIST_DELIM = Pattern.compile(",");
    
    private InternalBlackboardUserDao blackboardUserDao;

    @Autowired
    public void setBlackboardUserDao(InternalBlackboardUserDao blackboardUserDao) {
        this.blackboardUserDao = blackboardUserDao;
    }

    @Override
    public Set<BlackboardUser> getSessionChairs(long sessionId) {
        final BlackboardSessionImpl sessionImpl = this.getSession(sessionId);
        if (sessionImpl == null) {
            return null;
        }

        //Create a copy to trigger loading of the user data
        return ImmutableSet.copyOf(sessionImpl.getChairs());
    }

    @Override
    public Set<BlackboardUser> getSessionNonChairs(long sessionId) {
        final BlackboardSessionImpl sessionImpl = this.getSession(sessionId);
        if (sessionImpl == null) {
            return null;
        }

        //Create a copy to trigger loading of the user data
        return ImmutableSet.copyOf(sessionImpl.getNonChairs());
    }

    @Override
    public BlackboardSessionImpl getSession(long sessionId) {
        final EntityManager entityManager = this.getEntityManager();
        return entityManager.find(BlackboardSessionImpl.class, sessionId);
    }

    @Override
    //TODO need @OpenEntityManager
    public BlackboardSessionImpl getSessionByBlackboardId(long bbSessionId) {
        final NaturalIdQuery<BlackboardSessionImpl> query = this.createNaturalIdQuery(BlackboardSessionImpl.class);
        query.using(BlackboardSessionImpl_.bbSessionId, bbSessionId);
        
        return query.load();
    }

    @Override
    @Transactional
    public BlackboardSessionImpl createSession(SessionResponse sessionResponse, String guestUrl) {
        //Find the creator user
        final String creatorId = sessionResponse.getCreatorId();
        final BlackboardUser creator = this.blackboardUserDao.getOrCreateBlackboardUser(creatorId);
        
        //Create and populate a new blackboardSession
        final BlackboardSessionImpl blackboardSession = new BlackboardSessionImpl(sessionResponse.getSessionId(), creator);
        updateBlackboardSession(sessionResponse, blackboardSession);
        
        blackboardSession.setGuestUrl(guestUrl);

        //Persist and return the new session
        this.getEntityManager().persist(blackboardSession);
        return blackboardSession;
    }

    @Override
    @Transactional
    public BlackboardSessionImpl updateSession(SessionResponse sessionResponse) {
        //Find the existing blackboardSession
        final BlackboardSessionImpl blackboardSession = this.getSessionByBlackboardId(sessionResponse.getSessionId());
        if (blackboardSession == null) {
            //TODO should this automatically fall back to create?
            throw new IncorrectResultSizeDataAccessException("No BlackboardSession could be found for sessionId " + sessionResponse.getSessionId(), 1);
        }
        
        //Copy over the response data
        updateBlackboardSession(sessionResponse, blackboardSession);
        
        this.getEntityManager().persist(blackboardSession);
        return blackboardSession;
    }

    @Override
    @Transactional
    public void deleteSession(BlackboardSession session) {
        Validate.notNull(session, "session can not be null");
        
        final EntityManager entityManager = this.getEntityManager();
        if (!entityManager.contains(session)) {
            session = entityManager.merge(session);
        }
        entityManager.remove(session);        
    }

    /**
     * Sync all data from the {@link SessionResponse} to the {@link BlackboardSessionImpl}
     */
    private void updateBlackboardSession(SessionResponse sessionResponse, BlackboardSessionImpl blackboardSession) {
        blackboardSession.setAccessType(sessionResponse.getAccessType());
        blackboardSession.setAllowInSessionInvites(sessionResponse.isAllowInSessionInvites());
        blackboardSession.setBoundaryTime(sessionResponse.getBoundaryTime());
        blackboardSession.setChairNotes(sessionResponse.getChairNotes());
        blackboardSession.setEndTime(BlackboardDaoUtils.toDateTime(sessionResponse.getEndTime()));
        blackboardSession.setHideParticipantNames(sessionResponse.isHideParticipantNames());
        blackboardSession.setMaxCameras(sessionResponse.getMaxCameras());
        blackboardSession.setMaxTalkers(sessionResponse.getMaxTalkers());
        blackboardSession.setMustBeSupervised(sessionResponse.isMustBeSupervised());
        blackboardSession.setNonChairNotes(sessionResponse.getNonChairNotes());
        blackboardSession.setOpenChair(sessionResponse.isOpenChair());
        blackboardSession.setRaiseHandOnEnter(sessionResponse.isRaiseHandOnEnter());
        blackboardSession.setRecordingMode(sessionResponse.getRecordingModeType());
        blackboardSession.setRecordings(sessionResponse.isRecordings());
        blackboardSession.setReserveSeats(sessionResponse.getReserveSeats());
        blackboardSession.setSessionName(sessionResponse.getSessionName());
        blackboardSession.setStartTime(BlackboardDaoUtils.toDateTime(sessionResponse.getStartTime()));
        blackboardSession.setVersionId(sessionResponse.getVersionId());
        
        updateUserList(sessionResponse, blackboardSession, UserListType.CHAIR);
        
        updateUserList(sessionResponse, blackboardSession, UserListType.NON_CHAIR);
    }
    
    
    /**
     * Syncs the user list (chair or non-chair) from the {@link SessionResponse} to the {@link BlackboardSessionImpl}. Handles
     * creating/updating the associated {@link BlackboardUser} objects
     * 
     * @param sessionResponse Source of user list data
     * @param blackboardSession Destination to be updated with user list data
     * @param type The type of user list data to sync
     */
    private void updateUserList(SessionResponse sessionResponse, BlackboardSessionImpl blackboardSession, UserListType type) {
        final String userList = type.getUserList(sessionResponse);
        final String[] userIds = USER_LIST_DELIM.split(userList);
        
        final Set<BlackboardUser> existingUsers = type.getUserSet(blackboardSession);
        final Set<BlackboardUser> newUsers = new HashSet<BlackboardUser>(userIds.length);
        for (final String userId : userIds) {
            //find the DB user object for the chair
            final BlackboardUserImpl user = this.blackboardUserDao.getOrCreateBlackboardUser(userId);
            
            //Update the user's set of chaired sessions
            final boolean added = type.associateSession(user, blackboardSession);
            
            //User was modified, make sure we tell hibernate to persist them
            if (added) {
                this.blackboardUserDao.updateBlackboardUser(user);
            }
            
            //Add the user to the new set and make sure the user is in the existing set
            newUsers.add(user);
            existingUsers.add(user);
        }
        
        //Use this approach to remove any users that are no longer in the list. Mutating the existing
        //collection is slightly more expensive in CPU time but significantly less expensive for the
        //hibernate layer to persist
        for (final Iterator<BlackboardUser> existingUserItr = existingUsers.iterator(); existingUserItr.hasNext();) {
            final BlackboardUser existingUser = existingUserItr.next();
            //Check each existing user to see if they should no longer be a chair
            if (!newUsers.contains(existingUser)) {
                //Remove from existing chairs set
                existingUserItr.remove();
                
                //Update the user's associate with the session
                final BlackboardUserImpl user = this.blackboardUserDao.getBlackboardUser(existingUser.getUserId());
                final boolean removed = type.unassociateSession(user, blackboardSession);
                if (removed) {
                    this.blackboardUserDao.updateBlackboardUser(user);
                }
            }
        }
    }
    
    /**
     * Utility enum to hide the specific methods involved with updating chair vs
     * non-chair user to session associations.
     */
    private enum UserListType {
        CHAIR {
            @Override
            public String getUserList(SessionResponse sessionResponse) {
                return sessionResponse.getChairList();
            }

            @Override
            public Set<BlackboardUser> getUserSet(BlackboardSessionImpl blackboardSession) {
                return blackboardSession.getChairs();
            }

            @Override
            public boolean associateSession(BlackboardUserImpl user, BlackboardSession session) {
                return user.getChairedSessions().add(session);
            }

            @Override
            public boolean unassociateSession(BlackboardUserImpl user, BlackboardSession session) {
                return user.getChairedSessions().remove(session);
            }
        },
        NON_CHAIR{
            @Override
            public String getUserList(SessionResponse sessionResponse) {
                return sessionResponse.getNonChairList();
            }

            @Override
            public Set<BlackboardUser> getUserSet(BlackboardSessionImpl blackboardSession) {
                return blackboardSession.getNonChairs();
            }

            @Override
            public boolean associateSession(BlackboardUserImpl user, BlackboardSession session) {
                return user.getNonChairedSessions().add(session);
            }

            @Override
            public boolean unassociateSession(BlackboardUserImpl user, BlackboardSession session) {
                return user.getNonChairedSessions().remove(session);
            }
        };
        
        abstract String getUserList(SessionResponse sessionResponse);
        
        abstract Set<BlackboardUser> getUserSet(BlackboardSessionImpl blackboardSession);
        
        abstract boolean associateSession(BlackboardUserImpl user, BlackboardSession session);
        
        abstract boolean unassociateSession(BlackboardUserImpl user, BlackboardSession session);
    }
}
