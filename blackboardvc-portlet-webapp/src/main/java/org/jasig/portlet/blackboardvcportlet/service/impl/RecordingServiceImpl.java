package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletPreferences;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardListRecordingLong;
import com.elluminate.sas.BlackboardListRecordingLongResponseCollection;
import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.elluminate.sas.BlackboardRemoveRecording;
import com.elluminate.sas.BlackboardSuccessResponse;

@Service("recordingService")
public class RecordingServiceImpl implements RecordingService {
    private static final Logger logger = LoggerFactory.getLogger(RecordingService.class);

	private SASWebServiceOperations sasWebServiceTemplate;
	private ConferenceUserDao blackboardUserDao;
	private SessionDao blackboardSessionDao;
	private SessionRecordingDao sessionRecordingDao;
    
    
    @Autowired
	public void setBlackboardUserDao(ConferenceUserDao blackboardUserDao) {
        this.blackboardUserDao = blackboardUserDao;
    }

    @Autowired
    public void setBlackboardSessionDao(SessionDao blackboardSessionDao) {
        this.blackboardSessionDao = blackboardSessionDao;
    }

    @Autowired
    public void setSessionRecordingDao(SessionRecordingDao sessionRecordingDao) {
        this.sessionRecordingDao = sessionRecordingDao;
    }

    @Autowired
	public void setSasWebServiceTemplate(SASWebServiceOperations sasWebServiceTemplate)
	{
		this.sasWebServiceTemplate = sasWebServiceTemplate;
	}

//	/**
//     * Get the recordings for a session
//     * @param sessionId Long
//     * @return Set<RecordingShort>
//     */
//    public Set<SessionRecording> getRecordingsForSession(long sessionId)
//    {
//        return blackboardSessionDao.getSessionRecordings(sessionId);
//    }
//    
    /**
     * Get a specific recording
     * @param recordingId Long
     * @return RecordingShort
     */
    public SessionRecording getRecording(long recordingId)
    {
        return sessionRecordingDao.getSessionRecording(recordingId);
    }
    
    /**
     * Get the recordings for a user
     * @param uid String
     * @return Set<RecordingShort>
     */
    public Set<SessionRecording> getRecordingsForUser(String uid)
    {
        //TODO this is not bad if the data is all in cache but if it isn't it would be better to just run a query
        
        final ConferenceUser blackboardUser = this.blackboardUserDao.getUser(uid);
        
        final Set<SessionRecording> recordings = new LinkedHashSet<SessionRecording>();
        
        final Set<Session> chairedSessionsForUser = this.blackboardUserDao.getChairedSessionsForUser(blackboardUser);
        for (final Session blackboardSession : chairedSessionsForUser) {
            final Set<SessionRecording> sessionRecordings = this.blackboardSessionDao.getSessionRecordings(blackboardSession);
            recordings.addAll(sessionRecordings);
        }
        
        final Set<Session> nonChairedSessionsForUser = this.blackboardUserDao.getNonChairedSessionsForUser(blackboardUser);
        for (final Session blackboardSession : nonChairedSessionsForUser) {
            final Set<SessionRecording> sessionRecordings = this.blackboardSessionDao.getSessionRecordings(blackboardSession);
            recordings.addAll(sessionRecordings);
        }

        return recordings;
    }
    
    /**
     * Get recordings as Admin
     * @return Set<RecordingShort>
     */
    public Set<SessionRecording> getRecordingsForAdmin()
    {
        return sessionRecordingDao.getAllRecordings();
    }
   
    /**
     * Delete a recording
     * @param prefs PortletPreferences
     * @param recordingId Long
     * @throws Exception 
     */
    public void deleteRecording(PortletPreferences prefs, long recordingId) throws Exception
    {
        logger.debug("deleteRecording called");
        
        try
        {
			BlackboardRemoveRecording removeRecording = new BlackboardRemoveRecording();
			removeRecording.setRecordingId(recordingId);
			BlackboardSuccessResponse successResponse = (BlackboardSuccessResponse) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveRecording", removeRecording);
			logger.debug("removeRecording response:" + successResponse);
		}
        catch (Exception e)
        {
            logger.error("Exception caught calling Collaborate API",e);
            throw e;
        }
        logger.debug("Deleted recordingUrl");
             
        //TODO probably shouldn't delete it from our local db if the WS call fails
        sessionRecordingDao.deleteRecordings(recordingId);
        logger.debug("Deleted recordingShort");
    }
   
    /**
     * Updates the local recordings cache from Collaborate for a particular session
     *
	 * @param sessionId Long
	 * @return Set<RecordingShort>
     */
    public Set<SessionRecording> updateSessionRecordings(long sessionId)
    {
        final Session session = blackboardSessionDao.getSession(sessionId);
        if (session == null) {
            //TODO?
        }
        
        Set<SessionRecording> recordingList = new LinkedHashSet<SessionRecording>();
        try
        {
            BlackboardListRecordingLong listRecording = new BlackboardListRecordingLong();
			listRecording.setSessionId(sessionId);
			BlackboardListRecordingLongResponseCollection listRecordingShortResponseCollection = (BlackboardListRecordingLongResponseCollection)sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRecordingShort", listRecording);
			
			final List<BlackboardRecordingLongResponse> recordingResponses = listRecordingShortResponseCollection.getRecordingLongResponses();
			
			for (final BlackboardRecordingLongResponse recordingResponse : recordingResponses) {
			    final SessionRecording sessionRecording = sessionRecordingDao.createOrUpdateRecording(recordingResponse);
			    recordingList.add(sessionRecording);
			}
		}
        catch (Exception e)
        {
            //TODO this should get rethrown
            logger.error("Exception caught refreshing recordings",e);
        }
        return recordingList;
    }
}
