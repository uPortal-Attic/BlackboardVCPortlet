package org.jasig.portlet.blackboardvcportlet.service;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;

public interface UserService {
	
    //TODO need a different return type here
	public ConferenceUser getUserDetails(String searchTerm);
	
}
