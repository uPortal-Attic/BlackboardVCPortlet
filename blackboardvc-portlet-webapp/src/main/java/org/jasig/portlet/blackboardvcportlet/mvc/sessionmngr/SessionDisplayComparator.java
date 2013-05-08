package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.Comparator;

import org.jasig.portlet.blackboardvcportlet.data.Session;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class SessionDisplayComparator implements Comparator<Session> {
    public static final SessionDisplayComparator INSTANCE = new SessionDisplayComparator();
    
    @Override
    public int compare(Session o1, Session o2) {
        return ComparisonChain.start()
                .compare(o2.getStartTime(), o1.getStartTime(), Ordering.natural().nullsLast())
                .compare(o1.getSessionName(), o2.getSessionName(), Ordering.natural().nullsLast())
                .result();
    }
}
