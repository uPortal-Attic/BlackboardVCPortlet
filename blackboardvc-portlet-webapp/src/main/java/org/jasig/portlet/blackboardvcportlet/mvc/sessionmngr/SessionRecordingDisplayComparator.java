package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.Comparator;

import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class SessionRecordingDisplayComparator implements Comparator<SessionRecording> {
    public static final SessionRecordingDisplayComparator INSTANCE = new SessionRecordingDisplayComparator();
    
    @Override
    public int compare(SessionRecording o1, SessionRecording o2) {
        return ComparisonChain.start()
                .compare(o1.getRoomStart(), o2.getRoomStart(), Ordering.natural().nullsLast())
                .compare(o1.getRoomName(), o2.getRoomName(), Ordering.natural().nullsLast())
                .result();
    }
}
