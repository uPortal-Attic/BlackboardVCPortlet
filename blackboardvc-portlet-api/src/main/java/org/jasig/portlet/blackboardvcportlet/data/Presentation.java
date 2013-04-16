package org.jasig.portlet.blackboardvcportlet.data;

public interface Presentation {
	public String getDescription();

	public long getSize();

	public String getFilename();

	public ConferenceUser getCreator();
}
