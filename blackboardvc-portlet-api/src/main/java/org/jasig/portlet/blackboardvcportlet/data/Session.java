package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author Eric Dalquist
 */
public interface Session extends Serializable {

    long getSessionId();

    long getEntityVersion();
    
    DateTime getLastUpdated();

    String getGuestUrl();
    
    
    //********** properties below are direct copies from the BlackboardSessionResponse **********//

    long getBbSessionId();
    
    String getSessionName();

    DateTime getStartTime();

    DateTime getEndTime();
    
    ConferenceUser getCreator();
    
    Presentation getPresentation();

    int getBoundaryTime();

    AccessType getAccessType();

    boolean isRecordings();

    String getChairNotes();

    String getNonChairNotes();

    //TODO missing groupingList

    boolean isOpenChair();
    
    boolean isPermissionsOn();

    boolean isMustBeSupervised();

    RecordingMode getRecordingMode();

    int getMaxTalkers();

    int getMaxCameras();

    boolean isRaiseHandOnEnter();

    int getReserveSeats();
    
    boolean isSecureSignOn();

    long getVersionId();

    boolean isAllowInSessionInvites();

    boolean isHideParticipantNames();
    
    void setLaunchUrl(String launchUrl);
    
    String getLaunchUrl();

	String getTimeUntilJoin();
	DateTime getStartTimeWithBoundaryTime ();

	String getTimeFancyText(DateTime from, DateTime to);
}