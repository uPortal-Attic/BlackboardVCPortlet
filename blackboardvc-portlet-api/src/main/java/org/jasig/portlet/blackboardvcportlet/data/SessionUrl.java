package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

interface SessionUrl extends Serializable {

    String getDisplayName();

    void setDisplayName(String displayName);

    Date getLastUpdated();

    void setLastUpdated(Date lastUpdated);

    long getSessionId();

    void setSessionId(long sessionId);

    String getUrl();

    void setUrl(String url);

    String getUserId();

    void setUserId(String userId);

}