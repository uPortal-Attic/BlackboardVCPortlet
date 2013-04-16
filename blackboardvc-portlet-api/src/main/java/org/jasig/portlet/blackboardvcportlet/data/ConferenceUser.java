package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Map;

public interface ConferenceUser extends Serializable {

    long getUserId();

    String getEmail();

    String getDisplayName();
    
    void setDisplayName(String displayName);

    Map<String, String> getAttributes();

}