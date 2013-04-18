package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jasig.portlet.blackboardvcportlet.dao.ws.GlobalSettingsWSDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.support.DataAccessUtils;

import com.elluminate.sas.BlackboardGetServerConfigurationResponseCollection;
import com.elluminate.sas.BlackboardGetServerQuotasResponseCollection;
import com.elluminate.sas.BlackboardGetServerVersionResponseCollection;
import com.elluminate.sas.BlackboardServerConfigurationResponse;
import com.elluminate.sas.BlackboardServerQuotasResponse;
import com.elluminate.sas.BlackboardServerVersionResponse;

@RunWith(MockitoJUnitRunner.class)
public class GlobalSettingsWSDaoTest extends GlobalSettingsWSDaoImplIT {
	
	@Mock 
	private GlobalSettingsWSDao dao;
	
	@Before
	public void thebefore() {
		super.setGlobalSettingWSDao(dao);
	}
	
	@Test
	public void getServerConfigurationTest() throws Exception  {
		when(dao.getServerConfiguration()).thenReturn(getServerConfigMock());
		super.getServerConfigurationTest();
	}
	
	@Test
	public void getServerQuotaTest() throws Exception {
		when(dao.getServerQuota()).thenReturn(getServerQuotaMock());
		super.getServerQuotaTest();
	}
	
	@Test
	public void getServerVersionsTest() throws Exception {
		when(dao.getServerVersions()).thenReturn(getServerVersionsMock());
		super.getServerVersionsTest();
	}
	
	@Test
	public void setApiCallbackUrlTest() throws Exception {
		when(dao.setApiCallbackUrl(any(String.class))).thenReturn(true);
		super.setApiCallbackUrlTest();
	}
	
	private BlackboardServerVersionResponse getServerVersionsMock() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(BlackboardGetServerVersionResponseCollection.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final BlackboardGetServerVersionResponseCollection result = (BlackboardGetServerVersionResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/serverVersionsResponse.xml"));
        return DataAccessUtils.singleResult(result.getServerVersionResponses());
	}
	
	private BlackboardServerQuotasResponse getServerQuotaMock() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(BlackboardGetServerQuotasResponseCollection.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final BlackboardGetServerQuotasResponseCollection result = (BlackboardGetServerQuotasResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/serverQuotaResponse.xml"));
        return DataAccessUtils.singleResult(result.getServerQuotasResponses());
	}
	
	private BlackboardServerConfigurationResponse getServerConfigMock() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(BlackboardGetServerConfigurationResponseCollection.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final BlackboardGetServerConfigurationResponseCollection result = (BlackboardGetServerConfigurationResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/serverConfigurationResponse.xml"));
        return DataAccessUtils.singleResult(result.getServerConfigurationResponses());
	}
}
