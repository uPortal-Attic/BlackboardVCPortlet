package org.jasig.portlet.blackboardvcportlet.dao.impl;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public enum BlackboardDaoUtils {
    INSTANCE;
    
    public static DateTime toDateTime(long timestamp) {
        //The blackboard docs say all server-side time data is stored in UTC
        return new DateTime(timestamp, DateTimeZone.UTC);
    }
}
