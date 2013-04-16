package org.jasig.portlet.blackboardvcportlet.data;

import org.joda.time.DateTime;

public interface Multimedia {

	public abstract String getDescription();

	public abstract long getSize();

	public abstract DateTime getLastUpdated();

	public abstract long getMultimediaId();

	public abstract long getBbMultimediaId();

	public abstract ConferenceUser getCreator();

	public String getFilename();

}