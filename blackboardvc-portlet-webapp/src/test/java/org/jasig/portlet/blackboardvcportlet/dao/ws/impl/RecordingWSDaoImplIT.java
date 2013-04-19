package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.jasig.portlet.blackboardvcportlet.dao.ws.RecordingWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.elluminate.sas.BlackboardRecordingShortResponse;
import com.elluminate.sas.BlackboardSessionResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-applicationContext.xml")
public class RecordingWSDaoImplIT extends AbstractWSIT {
	
	@Autowired
	private RecordingWSDao dao;
	
	@Autowired
	private SessionWSDao sessionDao;
	
	@Before
	public void before() {
		form = buildSession();
		user = buildUser();
		session = sessionDao.createSession(user, form);
	}
	
	@After
	public void after() {
		List<BlackboardSessionResponse> sessions = sessionDao.getSessions(null, null, null, user.getUniqueId(), null, null, null);
		for(BlackboardSessionResponse session : sessions ) {
			
			sessionDao.deleteSession(session.getSessionId());			
		}
	}

	@Test
	public void getRecordingLongTest() {
		List<BlackboardRecordingLongResponse> recordingLongList = dao.getRecordingLong(user.getUniqueId(), null,session.getSessionId(), user.getEmail(), session.getStartTime(), session.getEndTime(), session.getSessionName());
		assertNotNull(recordingLongList);
	}

	@Test
	public void getRecordingShortTest(){
		List<BlackboardRecordingShortResponse> recordingShort = dao.getRecordingShort(user.getUniqueId(), null,session.getSessionId(), user.getEmail(), session.getStartTime(), session.getEndTime(), session.getSessionName());
		assertNotNull(recordingShort);
		
	}

	//The following methods don't work because that recording id is a fake and we don't have a test case.
	@Test
	public void removeRecordingTest() {
		
		final Long recordingId = new Long(455454);
		boolean removeRecording = dao.removeRecording(recordingId);
		assertTrue(removeRecording);
	}

	@Test
	public void buildRecordingUrlTest() {
		final Long recordingId = new Long(455454);
		String buildRecordingUrl = dao.buildRecordingUrl(recordingId);
		assertNotNull(buildRecordingUrl);
	}

	@Test
	public void updateRecordingSecureSignOnTest() {
		final Long recordingId = new Long(455454);
		boolean updateRecordingSecureSignOn = dao.updateRecordingSecureSignOn(recordingId, true);
		assertTrue(updateRecordingSecureSignOn);
	}

}
