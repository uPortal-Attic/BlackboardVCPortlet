package org.jasig.portlet.blackboardvcportlet.dao;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.UserSessionUrl;

public interface UserSessionUrlDao {

	public Set<UserSessionUrl> getAllUserSessionUrls();

	public UserSessionUrl getUserSessionUrlsBySessionAndUser(Session session, ConferenceUser user);

	public UserSessionUrl createUserSessionUrl(Session session, ConferenceUser user, String url);

	public void deleteOldSessionUrls(Session session, ConferenceUser user);

}
