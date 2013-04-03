package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * Entity class for storing recordings.
 * 
 * @author Eric Dalquist
 */
public interface SessionRecording extends Serializable {

    BlackboardSession getSession();

    String getRoomName();

    long getRecordingSize();

    DateTime getCreationDate();

    boolean isSecureSignOn();

    String getRecordingUrl();

    DateTime getRoomEnd();

    DateTime getRoomStart();

    long getBbRecordingId();

    long getRecordingId();

}