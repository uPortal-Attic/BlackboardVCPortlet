package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

public interface RecordingWSDao {
	public List getRecordingLong(int userId, int groupingId, int sessionId, int creatorId, Object startTime, Object endTime, String sessionName);

	public List getRecordingShort(int userId, int groupingId, int sessionId, int creatorId, Object startTime, Object endTime, String sessionName);
	
	public void removeRecording(int recordingId);
	
	public String buildRecordingUrl(int recordingId);
	
	public void updateRecordingSecureSignOn(int recordingId, String secureSignOn);
}
