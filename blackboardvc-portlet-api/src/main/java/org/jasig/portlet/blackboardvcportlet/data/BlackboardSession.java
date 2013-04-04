package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author Eric Dalquist
 */
public interface BlackboardSession extends Serializable {

    BlackboardUser getCreator();

    long getBbSessionId();

    long getEntityVersion();

    long getSessionId();

    boolean isHideParticipantNames();

    boolean isAllowInSessionInvites();

    long getVersionId();

    int getReserveSeats();

    boolean isRaiseHandOnEnter();

    int getMaxCameras();

    int getMaxTalkers();

    long getRecordingMode();

    boolean isMustBeSupervised();

    boolean isOpenChair();

    String getNonChairNotes();

    String getChairNotes();

    boolean isRecordings();

    long getAccessType();

    int getBoundaryTime();

    DateTime getEndTime();

    DateTime getStartTime();

    String getSessionName();
    
    DateTime getLastUpdated();

}