/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardListRecordingLongResponseCollection;
import com.elluminate.sas.BlackboardListRecordingShortResponseCollection;
import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSuccessResponse;
import com.elluminate.sas.BlackboardUrlResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-applicationContext.xml")
public class RecordingWSDaoImplTest extends RecordingWSDaoImplTestBase {
	@Mock
	private SASWebServiceOperations sasWebServiceOperations;
	@Mock
	private BlackboardSessionResponse session;
	@Mock
	private SessionWSDao sessionDao;
	
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		super.sessionDao = this.sessionDao;
		when(sessionDao.createSession(any(ConferenceUser.class), any(SessionForm.class))).thenReturn(session);
		dao.setSasWebServiceOperations(sasWebServiceOperations);
		super.before();
	}
	
	@Override
	@Test
	public void getRecordingLongTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/ListRecordingLong"), any(Object.class))).thenReturn(getSingleListOfRecordingLong());
		super.getRecordingLongTest();
	}

	@Override
	@Test
	public void getRecordingShortTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/ListRecordingShort"), any(Object.class))).thenReturn(getSingleListOfRecordingShort());
		super.getRecordingShortTest();
	}

	//The following methods don't work because that recording id is a fake and we don't have a test case.
	@Override
	@Test
	public void removeRecordingTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/RemoveRecording"), any(Object.class))).thenReturn(mockSuccessResponse());
		super.removeRecordingTest();
	}

	@Override
	@Test
	public void buildRecordingUrlTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/BuildRecordingUrl"), any(Object.class))).thenReturn(mockRecordingUrlObject());
		super.buildRecordingUrlTest();
	}

	@Override
	@Test
	public void updateRecordingSecureSignOnTest() throws Exception {
		when(sasWebServiceOperations.marshalSendAndReceiveToSAS(Matchers.contains("http://sas.elluminate.com/SetRecordingSecureSignOn"), any(Object.class))).thenReturn(mockSuccessResponse());
		super.updateRecordingSecureSignOnTest();
	}
	
	private BlackboardListRecordingLongResponseCollection getSingleListOfRecordingLong() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance("com.elluminate.sas");
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        BlackboardListRecordingLongResponseCollection response = (BlackboardListRecordingLongResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/singleListRecordingLongResponseCollection.xml"));
        
        return response;
	}
	
	private BlackboardListRecordingShortResponseCollection getSingleListOfRecordingShort() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance("com.elluminate.sas");
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        BlackboardListRecordingShortResponseCollection response = (BlackboardListRecordingShortResponseCollection)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/singleListRecordingShortResponseCollection.xml"));
        
        return response;
	}
	
	private BlackboardSuccessResponse mockSuccessResponse() {
		BlackboardSuccessResponse response = new BlackboardSuccessResponse();
		response.setSuccess(true);
		return response;
	}
	
	private BlackboardUrlResponse mockRecordingUrlObject() {
		BlackboardUrlResponse response = new BlackboardUrlResponse();
		response.setUrl("http://www.internet.com");
		return response;
	}
}
