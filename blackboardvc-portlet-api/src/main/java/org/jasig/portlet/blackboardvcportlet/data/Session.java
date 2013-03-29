package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

public interface Session extends Serializable {

    boolean isCurrUserCanEdit();

    void setCurrUserCanEdit(boolean currUserCanEdit);

    String getCreatorOrgUnit();

    void setCreatorOrgUnit(String creatorOrgUnit);

    long getAccessType();

    void setAccessType(long accessType);

    boolean isAllowInSessionInvites();

    void setAllowInSessionInvites(boolean allowInSessionInvites);

    int getBoundaryTime();

    void setBoundaryTime(int boundaryTime);

    String getChairList();

    void setChairList(String chairList);

    String getChairNotes();

    void setChairNotes(String chairNotes);

    String getCreatorId();

    void setCreatorId(String creatorId);

    Date getEndTime();

    void setEndTime(Date endTime);

    String getGroupingList();

    void setGroupingList(String groupingList);

    boolean isHideParticipantNames();

    void setHideParticipantNames(boolean hideParticipantNames);

    Date getLastUpdated();

    void setLastUpdated(Date lastUpdated);

    int getMaxCameras();

    void setMaxCameras(int maxCameras);

    int getMaxTalkers();

    void setMaxTalkers(int maxTalkers);

    boolean isMustBeSupervised();

    void setMustBeSupervised(boolean mustBeSupervised);

    String getNonChairList();

    void setNonChairList(String nonChairList);

    String getNonChairNotes();

    void setNonChairNotes(String nonChairNotes);

    boolean isOpenChair();

    void setOpenChair(boolean openChair);

    boolean isPermissionsOn();

    void setPermissionsOn(boolean permissionsOn);

    boolean isRaiseHandOnEnter();

    void setRaiseHandOnEnter(boolean raiseHandOnEnter);

    long getRecordingModeType();

    void setRecordingModeType(long recordingModeType);

    boolean isRecordings();

    void setRecordings(boolean recordings);

    int getReserveSeats();

    void setReserveSeats(int reserveSeats);

    boolean isSecureSignOn();

    void setSecureSignOn(boolean secureSignOn);

    long getSessionId();

    void setSessionId(long sessionId);

    String getSessionName();

    void setSessionName(String sessionName);

    Date getStartTime();

    void setStartTime(Date startTime);

    long getVersionId();

    void setVersionId(long versionId);

}