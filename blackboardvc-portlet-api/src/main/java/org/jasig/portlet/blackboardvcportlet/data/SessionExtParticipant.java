package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

public interface SessionExtParticipant extends Serializable {

    SessionExtParticipantId getSessionExtParticipantId();

    void setSessionExtParticipantId(SessionExtParticipantId sessionExtParticipantId);

    String getDisplay_name();

    void setDisplay_name(String display_name);

}