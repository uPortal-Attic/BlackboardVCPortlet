package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;

import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.elluminate.sas.BlackboardRecordingShortResponse;

public interface RecordingWSDao {
	/**
	 * Must specify at least one, can specify many
	 * @param userId
	 * @param groupingId
	 * @param sessionId
	 * @param creatorId
	 * @param startTime
	 * @param endTime
	 * @param sessionName
	 * @return
	 */
	public List<BlackboardRecordingLongResponse> getRecordingLong(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);

	/**
	 * Must specify at least one, can specify many
	 * @param userId
	 * @param groupingId
	 * @param sessionId
	 * @param creatorId
	 * @param startTime
	 * @param endTime
	 * @param sessionName
	 * @return
	 */
	public List<BlackboardRecordingShortResponse> getRecordingShort(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);
	
	public boolean removeRecording(long recordingId);
	
	public String buildRecordingUrl(long recordingId);
	
	public boolean updateRecordingSecureSignOn(long recordingId, boolean secureSignOn);
	
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations);
}
