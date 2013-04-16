package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.util.Comparator;

import org.jasig.portlet.blackboardvcportlet.data.Multimedia;

public class MultimediaNameComparator implements Comparator<Multimedia> {
    public static final MultimediaNameComparator INSTANCE = new MultimediaNameComparator();
    
    @Override
    public int compare(Multimedia o1, Multimedia o2) {
        return o1.getFilename().compareTo(o2.getFilename());
    }
}
