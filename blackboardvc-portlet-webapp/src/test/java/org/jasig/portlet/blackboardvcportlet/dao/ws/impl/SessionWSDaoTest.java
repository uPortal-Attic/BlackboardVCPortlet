package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.when;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardSessionResponseCollection;
import com.elluminate.sas.BlackboardSuccessResponse;
import com.elluminate.sas.BlackboardUrlResponse;

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
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/SetSession"), any(Object.class))).thenReturn(mockSession(false,false));
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
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/UpdateSession"), any(Object.class))).thenReturn(mockSession(false,false));
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
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(false,false));
		super.getSessionsByEmailAddressTest();
	}
	
	@Override
	@Test
	public void getSessionsBySessionIdTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(false,false));
		super.getSessionsBySessionIdTest();
	}
	
	@Override
	@Test
	public void clearSessionChairList() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ClearSession") , any(Object.class))).thenReturn(mockTrue());
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(true,false));
		super.clearSessionChairList();
	}
	
	@Override
	@Test
	public void clearSessionNonChairList() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ClearSession") , any(Object.class))).thenReturn(mockTrue());
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/ListSession"), any(Object.class))).thenReturn(mockSession(false,true));
		super.clearSessionNonChairList();
	}
	
	@Override
	@Test
	public void createSessionTelephony() {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(contains("http://sas.elluminate.com/SetSessionTelephony") , any(Object.class))).thenReturn(mockTrue());
	}
	
	private JAXBElement<BlackboardUrlResponse> mockSessionUrlResponse() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance("com.elluminate.sas");
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        @SuppressWarnings("unchecked")
		JAXBElement<BlackboardUrlResponse> response = (JAXBElement<BlackboardUrlResponse>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/mockSessionCreatorUrl.xml"));
        
        return response;
	}
	
	@SuppressWarnings("unchecked")
	private JAXBElement<BlackboardSessionResponseCollection> mockSession(boolean isEmptyChair, boolean isEmptyNonChair) throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance("com.elluminate.sas");
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        
        JAXBElement<BlackboardSessionResponseCollection> response;
        if (isEmptyChair && isEmptyNonChair) {
        	response = (JAXBElement<BlackboardSessionResponseCollection>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/mockSessionEmptyLists.xml"));
        } else if(isEmptyChair) {
			response = (JAXBElement<BlackboardSessionResponseCollection>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/mockSessionEmptyChair.xml"));
		} else if (isEmptyNonChair) {
			response = (JAXBElement<BlackboardSessionResponseCollection>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/mockSessionEmptyNonChair.xml"));
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
