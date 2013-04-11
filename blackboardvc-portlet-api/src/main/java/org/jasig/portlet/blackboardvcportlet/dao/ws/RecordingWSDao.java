package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.elluminate.sas.BlackboardRecordingShortResponse;

public interface RecordingWSDao {
	public List<BlackboardRecordingLongResponse> getRecordingLong(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);

	public List<BlackboardRecordingShortResponse> getRecordingShort(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);
	
	public boolean removeRecording(Long recordingId);
	
	public String buildRecordingUrl(Long recordingId);
	
	public boolean updateRecordingSecureSignOn(Long recordingId, boolean secureSignOn);
}
