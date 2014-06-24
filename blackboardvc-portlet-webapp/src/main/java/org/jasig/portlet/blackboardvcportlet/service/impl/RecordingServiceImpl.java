package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.RecordingWSDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.WebServiceClientException;

import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.elluminate.sas.BlackboardRecordingShortResponse;

@Service("recordingService")
public class RecordingServiceImpl implements RecordingService {
	
	private RecordingWSDao recordingWSDao;
	private SessionRecordingDao recordingDao;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public void setRecordingWSDao (RecordingWSDao recordingWSDao) {
		this.recordingWSDao = recordingWSDao;
	}
	
	@Autowired
	public void setRecordingDao (SessionRecordingDao dao) {
		this.recordingDao = dao;
	}
	
    @Override
    public void updateSessionRecordings(long sessionId, long startTime, long endTime) {
        //fetch the recording long information from the web service
		List<BlackboardRecordingLongResponse> recordingLongList = recordingWSDao.getRecordingLong(null, null, sessionId, null, startTime, endTime, null);
		BlackboardRecordingLongResponse recordingResponse = DataAccessUtils.singleResult(recordingLongList);
    	//post the information to the database
		recordingDao.createOrUpdateRecording(recordingResponse);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#recordingId, 'org.jasig.portlet.blackboardvcportlet.data.SessionRecording', 'view')")
    public SessionRecording getSessionRecording(long recordingId) {
        return this.recordingDao.getSessionRecording(recordingId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#recordingId, 'org.jasig.portlet.blackboardvcportlet.data.SessionRecording', 'edit')")
    public void updateSessionRecordingName(long recordingId, String roomName) {
        final SessionRecording sessionRecording = this.recordingDao.getSessionRecording(recordingId);
        sessionRecording.setRoomName(roomName);
        this.recordingDao.updateSessionRecording(sessionRecording);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasPermission(#recordingId, 'org.jasig.portlet.blackboardvcportlet.data.SessionRecording', 'delete')")
    public void removeRecording(long recordingId) {
        final SessionRecording sessionRecording = this.recordingDao.getSessionRecording(recordingId);
        
        try {
            this.recordingWSDao.removeRecording(sessionRecording.getBbRecordingId());
        }
        catch (WebServiceClientException e) {
            //See if the recording actually exists
            final List<BlackboardRecordingShortResponse> recordings = this.recordingWSDao.getRecordingShort(null, null, sessionRecording.getSession().getBbSessionId(), null, null, null, null);
            
            boolean exists = false;
            for (final BlackboardRecordingShortResponse recording : recordings) {
                if (recording.getRecordingId() == sessionRecording.getBbRecordingId()) {
                    exists = true;
                    break;
                }
            }
            
            //Recording exists but we failed to remove it, throw the exception
            if (exists) {
                throw e;
            }
            
            //Recording doesn't exist on the BB side, remove our local DB version
        }
        this.recordingDao.deleteRecording(sessionRecording);
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public int datafixRecordings(DateTime startDate, DateTime endDate) {
       int countErred = 0;
       //fetch the recording long information from the web service
        List<BlackboardRecordingLongResponse> recordingLongList = recordingWSDao.getRecordingLong(null, null, null, null, startDate.getMillis(), endDate.getMillis(), null);
        for(BlackboardRecordingLongResponse recordingResponse : recordingLongList) {
            try{
                //post the information to the database
                recordingDao.createOrUpdateRecording(recordingResponse);
            } catch (Exception ex) {
                logger.error("Error adding datafix for recording: " + recordingResponse.getRecordingId() + " for session : " + recordingResponse.getSessionId(), ex);
                countErred++;
            }
        }
        return countErred;
    }
}
