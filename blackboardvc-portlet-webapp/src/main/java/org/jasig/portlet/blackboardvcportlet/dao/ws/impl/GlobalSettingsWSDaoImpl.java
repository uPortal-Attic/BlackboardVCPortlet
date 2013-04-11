package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ws.GlobalSettingsWSDao;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardGetServerConfigurationResponseCollection;
import com.elluminate.sas.BlackboardGetServerQuotasResponseCollection;
import com.elluminate.sas.BlackboardGetServerVersionResponseCollection;
import com.elluminate.sas.BlackboardServerConfiguration;
import com.elluminate.sas.BlackboardServerConfigurationResponse;
import com.elluminate.sas.BlackboardServerQuotas;
import com.elluminate.sas.BlackboardServerQuotasResponse;
import com.elluminate.sas.BlackboardServerVersionResponse;
import com.elluminate.sas.BlackboardServerVersions;
import com.elluminate.sas.BlackboardSetApiCallbackUrl;
import com.elluminate.sas.BlackboardSuccessResponse;
import com.elluminate.sas.ObjectFactory;

@Service
public class GlobalSettingsWSDaoImpl implements GlobalSettingsWSDao {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalSettingsWSDaoImpl.class);

	private SASWebServiceOperations sasWebServiceTemplate;
	private String callbackURL;
	
	@Value("${bbc.callbackURL}")
	public void setCallbackURL(String value) {
		this.callbackURL = value;
	}
	
	@Autowired
	public void setSasWebServiceTemplate(SASWebServiceOperations sasWebServiceTemplate)
	{
		this.sasWebServiceTemplate = sasWebServiceTemplate;
	}
	
	@Override
	public BlackboardServerConfigurationResponse getServerConfiguration() {
		final JAXBElement<BlackboardServerConfiguration> request = new ObjectFactory().createGetServerConfiguration(null);
	    BlackboardGetServerConfigurationResponseCollection responseCollection = (BlackboardGetServerConfigurationResponseCollection) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/GetServerConfiguration", request);
		List<BlackboardServerConfigurationResponse> configResult = responseCollection.getServerConfigurationResponses();
		return DataAccessUtils.singleResult(configResult);
	}

	@Override
	public BlackboardServerQuotasResponse getServerQuota() {
		final JAXBElement<BlackboardServerQuotas> request = new ObjectFactory().createGetServerQuotas(null);
		BlackboardGetServerQuotasResponseCollection serverQuotasResponseCollection = (BlackboardGetServerQuotasResponseCollection)sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/GetServerQuotas", request);
		List<BlackboardServerQuotasResponse> quotaResult = serverQuotasResponseCollection.getServerQuotasResponses();
		return DataAccessUtils.singleResult(quotaResult);
	}

	@Override
	public BlackboardServerVersionResponse getServerVersions() {
		final JAXBElement<BlackboardServerVersions> request = new ObjectFactory().createGetServerVersions(null);
		BlackboardGetServerVersionResponseCollection serverVersionResponseCollection = (BlackboardGetServerVersionResponseCollection) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/GetServerVersions", request);
		List<BlackboardServerVersionResponse> versionResult = serverVersionResponseCollection.getServerVersionResponses();
		return DataAccessUtils.singleResult(versionResult);
		
	}

	@Override
	public boolean setApiCallbackUrl(String randomURLToken) {
		//create request object
		final BlackboardSetApiCallbackUrl apiCallbackRequest = new ObjectFactory().createBlackboardSetApiCallbackUrl();
		//create URL
		apiCallbackRequest.setApiCallbackUrl(callbackURL + (callbackURL.lastIndexOf('/') == (callbackURL.length() - 1) ? "" : "/") + randomURLToken);
		//send new URL to blackboard	
		JAXBElement<BlackboardSuccessResponse> jaxbApiCallbackUrlResponse = (JAXBElement<BlackboardSuccessResponse>) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetApiCallbackUrl", apiCallbackRequest);
		BlackboardSuccessResponse apiCallbackUrlResponse = jaxbApiCallbackUrlResponse.getValue();
		if(!apiCallbackUrlResponse.isSuccess()) {
			logger.warn("Issue sending blackboard api callback URL");
			return false;
		}
		return true;
		
	}

}
