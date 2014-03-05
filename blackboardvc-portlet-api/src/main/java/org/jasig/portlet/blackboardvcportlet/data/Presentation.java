package org.jasig.portlet.blackboardvcportlet.data;

public interface Presentation {
    long getPresentationId();
    
	String getDescription();

	long getSize();

	String getFilename();

	ConferenceUser getCreator();

	long getBbPresentationId();
}
