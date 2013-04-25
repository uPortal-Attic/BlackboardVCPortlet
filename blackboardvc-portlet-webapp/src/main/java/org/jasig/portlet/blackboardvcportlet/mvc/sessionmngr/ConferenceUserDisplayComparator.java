package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.util.Comparator;

public class ConferenceUserDisplayComparator implements Comparator<ConferenceUser> {
    public static final ConferenceUserDisplayComparator INSTANCE = new ConferenceUserDisplayComparator();
    
    @Override
    public int compare(ConferenceUser o1, ConferenceUser o2) {
        return ComparisonChain.start()
                .compare(o1.getDisplayName(), o2.getDisplayName(), Ordering.natural().nullsLast())
                .result();
    }
}
