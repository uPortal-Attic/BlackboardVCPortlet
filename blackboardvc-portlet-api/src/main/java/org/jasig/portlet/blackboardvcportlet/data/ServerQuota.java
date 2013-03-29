package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;
import java.util.Date;

public interface ServerQuota extends Serializable {

    long getDiskQuota();

    void setDiskQuota(long diskQuota);

    long getDiskQuotaAvailable();

    void setDiskQuotaAvailable(long diskQuotaAvailable);

    Date getLastUpdated();

    void setLastUpdated(Date lastUpdated);

    int getSessionQuota();

    void setSessionQuota(int sessionQuota);

    int getSessionQuotaAvailable();

    void setSessionQuotaAvailable(int sessionQuotaAvailable);

}