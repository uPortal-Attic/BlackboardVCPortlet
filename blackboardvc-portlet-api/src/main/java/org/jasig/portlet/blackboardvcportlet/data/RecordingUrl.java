package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

interface RecordingUrl extends Serializable {

    Date getLastUpdated();

    void setLastUpdated(Date lastUpdated);

    void setRecordingId(Long recordingId);

    Long getRecordingId();

    String getUrl();

    void setUrl(String url);

}