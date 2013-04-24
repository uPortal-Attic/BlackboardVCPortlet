package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import java.util.Comparator;

public class ConferenceUserDisplayNameComparator implements Comparator<ConferenceUser> {
    public static final ConferenceUserDisplayNameComparator INSTANCE = new ConferenceUserDisplayNameComparator();
    
    @Override
    public int compare(ConferenceUser o1, ConferenceUser o2) {

		if (o1.getDisplayName() != null && o2.getDisplayName() == null)
		{
			return 1;
		}
		else if (o1.getDisplayName() == null && o2.getDisplayName() != null)
		{
			return -1;
		}
		else if (o1.getDisplayName() == null && o2.getDisplayName() == null)
		{
			return 0;
		}

        return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
}
