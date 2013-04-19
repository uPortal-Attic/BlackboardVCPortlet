package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.jasig.portlet.blackboardvcportlet.dao.ws.MultimediaWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardMultimediaResponseCollection;
import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSuccessResponse;

@RunWith(SpringJUnit4ClassRunner.class)
public class MultimediaWSDaoTest extends MultimediaWSDaoIT {
	
	@Mock
	private SASWebServiceOperations sasWebServiceOperations;
	
	@Mock
	private SessionWSDao sessionDao;
	
	@Mock BlackboardSessionResponse session;
	
	@Autowired
	private MultimediaWSDao dao;
	
	@Override
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		dao.setSasWebServiceOperations(sasWebServiceOperations);
		setMMWSDao(dao);
		setSessionDao(sessionDao);
		when(sessionDao.createSession(any(ConferenceUser.class), any(SessionForm.class))).thenReturn(session);
		when(session.getSessionId()).thenReturn((long)1);
		when(sessionDao.getSessions(any(String.class), any(String.class), any(Long.class), any(String.class), any(Long.class), any(Long.class), any(String.class))).thenReturn(new ArrayList<BlackboardSessionResponse>());
		super.before();
	}

	@Override
	@Test
	public void getRepositoryMultimediasTest () throws Exception {
		//return empty set like it is expecting
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(any(String.class), any(Object.class))).thenReturn(new JAXBElement<BlackboardMultimediaResponseCollection>(new QName("http://sas.elluminate.com"), BlackboardMultimediaResponseCollection.class, new BlackboardMultimediaResponseCollection()));
		super.getRepositoryMultimediasTest();
	}
	
	@Override
	@Test
	public void getSessionRepositoryMultimediasTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(any(String.class), any(Object.class))).thenReturn(new JAXBElement<BlackboardMultimediaResponseCollection>(new QName("http://sas.elluminate.com"), BlackboardMultimediaResponseCollection.class, new BlackboardMultimediaResponseCollection()));
		super.getSessionRepositoryMultimediasTest();
	}
	
	@Override
	@Test
	public void uploadRepositoryMultimediaTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(any(String.class), any(Object.class))).thenReturn(getSingleMultimedia());
		super.uploadRepositoryMultimediaTest();
	}
	
	@Override
	@Test
	public void createSessionMultimediaTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(any(String.class), any(Object.class))).thenReturn(getSingleMultimedia());
		super.createSessionMultimediaTest();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Test
	public void linkSessionToMultimediaTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(any(String.class), any(Object.class))).thenAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				if(invocation.getArguments()[0].equals("http://sas.elluminate.com/SetSessionMultimedia")) {
					BlackboardSuccessResponse blackboardSuccessResponse = new BlackboardSuccessResponse();
					blackboardSuccessResponse.setSuccess(true);
					return blackboardSuccessResponse;
				} else {
					return getSingleMultimedia();
				}
			}
			
		});
		
		super.linkSessionToMultimediaTest();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Test
	public void removeRepositoryMultimediaTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(any(String.class), any(Object.class))).thenAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				if(invocation.getArguments()[0].equals("http://sas.elluminate.com/RemoveRepositoryMultimedia")) {
					BlackboardSuccessResponse blackboardSuccessResponse = new BlackboardSuccessResponse();
					blackboardSuccessResponse.setSuccess(true);
					return blackboardSuccessResponse;
				} else {
					return getSingleMultimedia();
				}
			}
			
		});
		super.removeRepositoryMultimediaTest();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Test
	public void removeSessionMultimediaTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(any(String.class), any(Object.class))).thenAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				if(invocation.getArguments()[0].equals("http://sas.elluminate.com/RemoveRepositoryMultimedia")
						|| invocation.getArguments()[0].equals("http://sas.elluminate.com/SetSessionMultimedia")) {
					BlackboardSuccessResponse blackboardSuccessResponse = new BlackboardSuccessResponse();
					blackboardSuccessResponse.setSuccess(true);
					return blackboardSuccessResponse;
				} else {
					return getSingleMultimedia();
				}
			}
			
		});
		
		super.removeSessionMultimediaTest();
	}
	
	@SuppressWarnings("unchecked")
	private JAXBElement<BlackboardMultimediaResponseCollection> getSingleMultimedia() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance("com.elluminate.sas");
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<BlackboardMultimediaResponseCollection> response = (JAXBElement<BlackboardMultimediaResponseCollection>)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/singleListRepositoryMultimediaResponseCollection.xml"));
        
        return response;
	}

}
