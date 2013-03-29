package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

public interface ServerConfiguration extends Serializable {

    Date getLastUpdated();

    void setLastUpdated(Date lastUpdated);

    int getBoundaryTime();

    void setBoundaryTime(int boundaryTime);

    int getMaxAvailableCameras();

    void setMaxAvailableCameras(int maxAvailableCameras);

    int getMaxAvailableTalkers();

    void setMaxAvailableTalkers(int maxAvailableTalkers);

    char getMayUseSecureSignOn();

    void setMayUseSecureSignOn(char mayUseSecureSignOn);

    char getMustReserveSeats();

    void setMustReserveSeats(char mustReserveSeats);

    char getRaiseHandOnEnter();

    void setRaiseHandOnEnter(char raiseHandOnEnter);

    String getTimezone();

    void setTimezone(String timezone);

}