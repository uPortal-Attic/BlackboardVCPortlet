package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardSessionResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-applicationContext.xml")
public class SessionWSDaoIT {
	
	@Autowired
	private SessionWSDao dao;
	
	private BlackboardSessionResponse session;
	private SessionForm form;
	private ConferenceUser user;
	
	@Before
	public void before() {
		form = buildSession();
		user = buildUser();
		session = dao.createSession(user, form);
	}
	
	@After
	public void after() {
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, null, user.getEmail(), null, null, null);
		for(BlackboardSessionResponse session : sessions ) {
			dao.deleteSession(session.getSessionId());			
		}
	}
	
	@Test
	public void createSessionTest() {
		//built session in before
		assertNotNull(session);
		
		assertEquals(session.getBoundaryTime(), form.getBoundaryTime());
		assertEquals(session.getEndTime(),form.getEndTime().getMillis());
		assertEquals(session.getStartTime(), form.getStartTime().getMillis());
		
		assertEquals(session.getCreatorId(), user.getEmail());
	}
	
	@Test
	public void updateSessionTest() {
		form.setNewSession(false);
		form.setSessionId(session.getSessionId());
		
		//go from 3 to 4 (four hour meeting)
		form.setEndTime((new DateTime()).plusHours(4).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0));
		BlackboardSessionResponse updateSession = dao.updateSession(user, form);
		assertNotNull(updateSession);
		assertEquals(form.getEndTime().getMillis(),updateSession.getEndTime());
	}
	
	@Test
	public void buildSessionUrlTest() {
		String url = dao.buildSessionUrl(session.getSessionId(), "GUEST");
		assertNotNull(url);
		
	}
	
	@Test
	public void getSessionsByEmailAddressTest() {
		
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, null, session.getCreatorId(), null, null, null);
		assertNotNull(sessions);
		assertEquals(DataAccessUtils.singleResult(sessions).getSessionId(), session.getSessionId());
	}
	
	@Test
	public void getSessionsBySessionIdTest() {
		
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, session.getSessionId(), null, null, null, null);
		assertNotNull(sessions);
		assertEquals(DataAccessUtils.singleResult(sessions).getSessionId(), session.getSessionId());
	}
	
	@Test
	public void clearSessionChairList() {
		assertTrue(dao.clearSessionChairList(session.getSessionId()));
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, session.getSessionId(), null, null, null, null);
		BlackboardSessionResponse singleSession = DataAccessUtils.singleResult(sessions);
		assertTrue(singleSession.getChairList().isEmpty());
	}
	
	@Test
	public void clearSessionNonChairList() {
		assertTrue(dao.clearSessionNonChairList(session.getSessionId()));
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, session.getSessionId(), null, null, null, null);
		BlackboardSessionResponse singleSession = DataAccessUtils.singleResult(sessions);
		assertTrue(singleSession.getNonChairList().isEmpty());
	}
	
	private ConferenceUser buildUser() {
		ConferenceUser user = new ConferenceUser() {

			private static final long serialVersionUID = 1L;
			private String username = "test";
			private String email = "my-test@example.com";
			private long id = 1;
			
			@Override
			public void setDisplayName(String displayName) {
				username = displayName;
				
			}
			
			@Override
			public long getUserId() {
				return id;
			}
			
			@Override
			public String getEmail() {
				return email;
			}
			
			@Override
			public String getDisplayName() {
				return username;
			}
			
			@Override
			public Map<String, String> getAttributes() {
				return null;
			}
		};
		return user;
		
	}

	private SessionForm buildSession() {
		SessionForm newForm = new SessionForm();
		newForm.setAllowInSessionInvites(false);
		newForm.setBoundaryTime(0);
		newForm.setHideParticipantNames(false);
		newForm.setMaxCameras(1);
		newForm.setMaxTalkers(1);
		newForm.setMustBeSupervised(false);
		newForm.setNewSession(true);
		newForm.setPermissionsOn(false);
		newForm.setRaiseHandOnEnter(false);
		newForm.setSessionName("Test session");
		
		//start date/time
		newForm.setStartDate(new DateMidnight());
		newForm.setStartHour(0);
		newForm.setStartMinute(0);
		newForm.setStartTime((new DateTime()).plusHours(2).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0));
		
		//end date/time
		newForm.setEndDate(new DateMidnight());
		newForm.setEndHour(1);
		newForm.setEndMinute(0);
		newForm.setEndTime((new DateTime()).plusHours(3).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0));
		
		return newForm;
	}
	

}
