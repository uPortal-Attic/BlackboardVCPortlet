package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.Comparator;

import org.jasig.portlet.blackboardvcportlet.data.Multimedia;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class MultimediaDisplayComparator implements Comparator<Multimedia> {
    public static final MultimediaDisplayComparator INSTANCE = new MultimediaDisplayComparator();
    
    @Override
    public int compare(Multimedia o1, Multimedia o2) {
        return ComparisonChain.start()
                .compare(o1.getFilename(), o2.getFilename(), Ordering.natural().nullsLast())
                .compare(o1.getLastUpdated(), o2.getLastUpdated(), Ordering.natural().nullsLast())
                .result();
    }
}
