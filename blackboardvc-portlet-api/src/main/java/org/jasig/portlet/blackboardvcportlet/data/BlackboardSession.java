package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

import org.joda.time.DateTime;

public interface BlackboardSession extends Serializable {

    BlackboardUser getCreator();

    long getBbSessionId();

    long getEntityVersion();

    long getSessionId();

    void setHideParticipantNames(boolean hideParticipantNames);

    boolean isHideParticipantNames();

    void setAllowInSessionInvites(boolean allowInSessionInvites);

    boolean isAllowInSessionInvites();

    void setVersionId(long versionId);

    long getVersionId();

    void setReserveSeats(int reserveSeats);

    int getReserveSeats();

    void setRaiseHandOnEnter(boolean raiseHandOnEnter);

    boolean isRaiseHandOnEnter();

    void setMaxCameras(int maxCameras);

    int getMaxCameras();

    void setMaxTalkers(int maxTalkers);

    int getMaxTalkers();

    void setRecordingMode(long recordingMode);

    long getRecordingMode();

    void setMustBeSupervised(boolean mustBeSupervised);

    boolean isMustBeSupervised();

    void setOpenChair(boolean openChair);

    boolean isOpenChair();

    void setNonChairNotes(String nonChairNotes);

    String getNonChairNotes();

    void setChairNotes(String chairNotes);

    String getChairNotes();

    void setRecordings(boolean recordings);

    boolean isRecordings();

    void setAccessType(long accessType);

    long getAccessType();

    void setBoundaryTime(int boundaryTime);

    int getBoundaryTime();

    void setEndTime(DateTime endTime);

    DateTime getEndTime();

    void setStartTime(DateTime startTime);

    DateTime getStartTime();

    void setSessionName(String sessionName);

    String getSessionName();

}