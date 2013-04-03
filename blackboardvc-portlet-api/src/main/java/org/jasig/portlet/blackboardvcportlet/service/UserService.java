package org.jasig.portlet.blackboardvcportlet.service;

import org.jasig.portlet.blackboardvcportlet.data.BlackboardUser;

public interface UserService {
	
    //TODO need a different return type here
	public BlackboardUser getUserDetails(String searchTerm);
	
}
