package org.jasig.portlet.blackboardvcportlet.dao.ws;

public interface GlobalSettingsWSDao {
	public void getServerConfiguration();
	public void getServerQuota();
	public void getServerVersions();
	public void setApiCallbackUrl(String callbackURL);
}
