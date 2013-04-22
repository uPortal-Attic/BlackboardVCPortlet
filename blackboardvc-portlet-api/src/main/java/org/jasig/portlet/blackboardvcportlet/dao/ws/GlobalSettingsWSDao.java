package org.jasig.portlet.blackboardvcportlet.dao.ws;

import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;

import com.elluminate.sas.BlackboardServerConfigurationResponse;
import com.elluminate.sas.BlackboardServerQuotasResponse;
import com.elluminate.sas.BlackboardServerVersionResponse;

public interface GlobalSettingsWSDao {
	public void setSasWebServiceTemplate(SASWebServiceOperations sasWebServiceTemplate);
	public BlackboardServerConfigurationResponse getServerConfiguration();
	public BlackboardServerQuotasResponse getServerQuota();
	public BlackboardServerVersionResponse getServerVersions();
	public boolean setApiCallbackUrl(String randomCallbackUrl);
}
