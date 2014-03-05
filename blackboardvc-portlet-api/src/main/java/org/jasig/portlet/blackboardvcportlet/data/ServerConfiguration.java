package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public interface ServerConfiguration extends Serializable {

    DateTime getLastUpdated();

    long getEntityVersion();

    DateTimeZone getTimezone();

    boolean isMustReserveSeats();

    boolean isMayUseSecureSignOn();

    boolean isMayUseTelephony();

    boolean isRaiseHandOnEnter();

    int getMaxAvailableCameras();

    int getMaxAvailableTalkers();

    int getBoundaryTime();
    
    String getRandomCallbackUrl();
}