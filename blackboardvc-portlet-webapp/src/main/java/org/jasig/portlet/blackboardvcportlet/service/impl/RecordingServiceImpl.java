package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.util.List;

import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.RecordingWSDao;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardRecordingLongResponse;

@Service("recordingService")
public class RecordingServiceImpl implements RecordingService {
	
	private RecordingWSDao wsDao;
	private SessionRecordingDao recordingDao;
	
	@Autowired
	public void setRecordingWSDao (RecordingWSDao wsDao) {
		this.wsDao = wsDao;
	}
	
	@Autowired
	public void setRecordingDao (SessionRecordingDao dao) {
		this.recordingDao = dao;
	}
	
    @Override
    public void updateSessionRecordings(long sessionId, long startTime, long endTime) {
        //fetch the recording long information from the web service
		List<BlackboardRecordingLongResponse> recordingLongList = wsDao.getRecordingLong(null, null, sessionId, null, startTime, endTime, null);
		BlackboardRecordingLongResponse recordingResponse = DataAccessUtils.singleResult(recordingLongList);
    	//post the information to the database
		recordingDao.createOrUpdateRecording(recordingResponse);
    }
}
