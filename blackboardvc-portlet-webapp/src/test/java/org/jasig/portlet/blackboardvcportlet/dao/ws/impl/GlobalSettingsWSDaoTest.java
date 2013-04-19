package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jasig.portlet.blackboardvcportlet.dao.ws.GlobalSettingsWSDao;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardGetServerConfigurationResponseCollection;
import com.elluminate.sas.BlackboardGetServerQuotasResponseCollection;
import com.elluminate.sas.BlackboardGetServerVersionResponseCollection;
import com.elluminate.sas.BlackboardSuccessResponse;

@RunWith(SpringJUnit4ClassRunner.class)
public class GlobalSettingsWSDaoTest extends GlobalSettingsWSDaoImplIT {
	
	@Autowired 
	private GlobalSettingsWSDao dao;
	
	@Mock
	private SASWebServiceOperations sasWebServiceTemplate;
	
	@Before
	public void thebefore() {
		MockitoAnnotations.initMocks(this);
		dao.setSasWebServiceTemplate(sasWebServiceTemplate);
		super.setGlobalSettingWSDao(dao);
	}
	
	@Test
	public void getServerConfigurationTest() throws Exception  {
		when(sasWebServiceTemplate.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/GetServerConfiguration"), any(Object.class))).thenReturn(getServerConfigMock());
		super.getServerConfigurationTest();
	}
	
	@Test
	public void getServerQuotaTest() throws Exception {
		when(sasWebServiceTemplate.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/GetServerQuota"), any(Object.class))).thenReturn(getServerQuotaMock());
		super.getServerQuotaTest();
	}
	
	@Test
	public void getServerVersionsTest() throws Exception {
		when(sasWebServiceTemplate.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/GetServerVersion"), any(Object.class))).thenReturn(getServerVersionsMock());
		super.getServerVersionsTest();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void setApiCallbackUrlTest() throws Exception {
		when(sasWebServiceTemplate.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/SetApiCallbackUrl"), any(Object.class))).thenAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				BlackboardSuccessResponse blackboardSuccessResponse = new BlackboardSuccessResponse();
				blackboardSuccessResponse.setSuccess(true);
				return blackboardSuccessResponse;
			}
			
		});
		super.setApiCallbackUrlTest();
	}
	
	private BlackboardGetServerVersionResponseCollection getServerVersionsMock() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(BlackboardGetServerVersionResponseCollection.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final BlackboardGetServerVersionResponseCollection result = (BlackboardGetServerVersionResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/serverVersionsResponse.xml"));
        return result;
	}
	
	private BlackboardGetServerQuotasResponseCollection getServerQuotaMock() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(BlackboardGetServerQuotasResponseCollection.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final BlackboardGetServerQuotasResponseCollection result = (BlackboardGetServerQuotasResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/serverQuotaResponse.xml"));
        return result;
	}
	
	private BlackboardGetServerConfigurationResponseCollection getServerConfigMock() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(BlackboardGetServerConfigurationResponseCollection.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final BlackboardGetServerConfigurationResponseCollection result = (BlackboardGetServerConfigurationResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/serverConfigurationResponse.xml"));
        return result;
	}
}
