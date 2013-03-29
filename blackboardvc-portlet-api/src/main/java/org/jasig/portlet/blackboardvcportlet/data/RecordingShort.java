package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity class for storing recordings.
 * 
 * @author Eric Dalquist
 */
public interface RecordingShort extends Serializable {

    String getReadableFileSize();

    void setReadableFileSize(String readableFileSize);

    String getChairList();

    void setChairList(String chairList);

    String getNonChairList();

    void setNonChairList(String nonChairList);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    boolean isCurrUserCanDelete();

    void setCurrUserCanDelete(boolean currUserCanDelete);

    String getRecordingUrl();

    void setRecordingUrl(String recordingUrl);

    long getCreationDate();

    void setCreationDate(long creationDate);

    long getRecordingId();

    void setRecordingId(long recordingId);

    long getRecordingSize();

    void setRecordingSize(long recordingSize);

    String getRoomName();

    void setRoomName(String roomName);

    long getSessionId();

    void setSessionId(long sessionId);

}