package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Map;

public interface BlackboardUser extends Serializable {

    long getUserId();

    String getEmail();

    String getDisplayName();

    Map<String, String> getAttributes();

    int hashCode();

    boolean equals(Object obj);
}