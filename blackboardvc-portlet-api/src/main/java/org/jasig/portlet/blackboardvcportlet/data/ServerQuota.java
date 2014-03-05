package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

import org.joda.time.DateTime;

public interface ServerQuota extends Serializable {

    long getDiskQuota();

    long getDiskQuotaAvailable();

    DateTime getLastUpdated();

    int getSessionQuota();

    int getSessionQuotaAvailable();

}