package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

public interface SessionPresentation extends Serializable {

    void setDateUploaded(Date dateUploaded);

    Date getDateUploaded();

    void setCreatorId(String creatorId);

    String getCreatorId();

    void setDescription(String description);

    String getDescription();

    void setFileName(String fileName);

    String getFileName();

    void setSessionId(String sessionId);

    String getSessionId();

    void setPresentationId(long presentationId);

    long getPresentationId();

}