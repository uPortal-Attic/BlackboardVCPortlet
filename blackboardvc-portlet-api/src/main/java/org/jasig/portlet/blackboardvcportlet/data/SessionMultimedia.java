package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

interface SessionMultimedia extends Serializable {

    long getMultimediaId();

    void setMultimediaId(long multimediaId);

    String getSessionId();

    void setSessionId(String sessionId);

    String getFileName();

    void setFileName(String fileName);

    String getDescription();

    void setDescription(String description);

    String getCreatorId();

    void setCreatorId(String creatorId);

    Date getDateUploaded();

    void setDateUploaded(Date dateUploaded);

}