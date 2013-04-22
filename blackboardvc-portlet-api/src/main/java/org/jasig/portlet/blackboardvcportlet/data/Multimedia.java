package org.jasig.portlet.blackboardvcportlet.data;

import org.joda.time.DateTime;

public interface Multimedia {
    long getMultimediaId();

	String getDescription();

	long getSize();

	DateTime getLastUpdated();

	long getBbMultimediaId();

	ConferenceUser getCreator();

	String getFilename();

}