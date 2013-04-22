package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jasig.portlet.blackboardvcportlet.dao.ws.MultimediaWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.jasig.springframework.mockito.MockitoFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardMultimediaResponseCollection;
import com.elluminate.sas.BlackboardSessionResponseCollection;
import com.elluminate.sas.BlackboardSuccessResponse;
import com.elluminate.sas.BlackboardUrlResponse;
import com.elluminate.sas.ObjectFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-applicationContext.xml")
public class SessionWSDaoTest extends SessionWSDaoTestBase {
	
	@Mock
	private SASWebServiceOperations sasWebServiceOperations;
	
	@Autowired
	private SessionWSDao dao;
	
	@Override
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/SetSession"), any(Object.class))).thenReturn(mockSession(false));
		super.dao.setSasWebServiceOperations(sasWebServiceOperations);
		super.before();	
	}
	
	@Override
	@After
	public void after() throws Exception {
		//do nothing (no cleanup needed)
	}

	@Override
	@Test
	public void createSessionTest() {
		super.createSessionTest();
	}
	
	@Override
	@Test
	public void updateSessionTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/UpdateSession"), any(Object.class))).thenReturn(mockSession(false));
		super.updateSessionTest();
	}
	
	@Override
	@Test
	public void buildSessionGuestUrlTest() throws Exception {
		//final Object urlResponseObject = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/BuildSessionUrl", buildSessionUrlRequest);
        //JAXBElement<BlackboardUrlResponse> jaxbResponse = (JAXBElement<BlackboardUrlResponse>) urlResponseObject;
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/BuildSessionUrl"), any(Object.class))).thenReturn(mockSessionUrlResponse());
		super.buildSessionGuestUrlTest();
	}
	
	/**
	 * This test case assumes the creator is initially added as a chair (moderator)
	 * @throws Exception 
	 */
	@Override
	@Test
	public void buildSessionUrlTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/BuildSessionUrl"), any(Object.class))).thenReturn(mockSessionUrlResponse());
		super.buildSessionUrlTest();
	}
	
	@Override
	@Test
	public void getSessionsByEmailAddressTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(false));
		super.getSessionsByEmailAddressTest();
	}
	
	@Override
	@Test
	public void getSessionsBySessionIdTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(false));
		super.getSessionsBySessionIdTest();
	}
	
	@Override
	@Test
	public void clearSessionChairList() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ClearSession") , any(Object.class))).thenReturn(mockTrue());
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(true));
		super.clearSessionChairList();
	}
	
	@Override
	@Test
	public void clearSessionNonChairList() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ClearSession") , any(Object.class))).thenReturn(mockTrue());
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(true));
		super.clearSessionNonChairList();
	}
	
	private JAXBElement<BlackboardUrlResponse> mockSessionUrlResponse() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance("com.elluminate.sas");
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        @SuppressWarnings("unchecked")
		JAXBElement<BlackboardUrlResponse> response = (JAXBElement<BlackboardUrlResponse>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/mockSessionCreatorUrl.xml"));
        
        return response;
	}
	
	@SuppressWarnings("unchecked")
	private JAXBElement<BlackboardSessionResponseCollection> mockSession(boolean isEmptyLists) throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance("com.elluminate.sas");
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        
        JAXBElement<BlackboardSessionResponseCollection> response;
		if(isEmptyLists) {
			response = (JAXBElement<BlackboardSessionResponseCollection>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/mockSessionEmptyLists.xml"));
		} else {
			response = (JAXBElement<BlackboardSessionResponseCollection>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/mockSessionResponse.xml"));
		}
        
        return response;
	}
	
	private BlackboardSuccessResponse mockTrue() {
		BlackboardSuccessResponse response = new BlackboardSuccessResponse();
		response.setSuccess(true);
		return response;
	}
}
