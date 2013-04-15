package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.Comparator;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;

public class ConferenceUserDisplayNameComparator implements Comparator<ConferenceUser> {
    public static final ConferenceUserDisplayNameComparator INSTANCE = new ConferenceUserDisplayNameComparator();
    
    @Override
    public int compare(ConferenceUser o1, ConferenceUser o2) {
        return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
}
