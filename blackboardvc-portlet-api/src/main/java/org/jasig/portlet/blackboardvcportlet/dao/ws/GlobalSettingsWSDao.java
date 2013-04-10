package org.jasig.portlet.blackboardvcportlet.dao.ws;

import com.elluminate.sas.BlackboardServerConfigurationResponse;
import com.elluminate.sas.BlackboardServerQuotasResponse;
import com.elluminate.sas.BlackboardServerVersionResponse;

public interface GlobalSettingsWSDao {
	public BlackboardServerConfigurationResponse getServerConfiguration();
	public BlackboardServerQuotasResponse getServerQuota();
	public BlackboardServerVersionResponse getServerVersions();
	public void setApiCallbackUrl(String randomCallbackUrl);
}
