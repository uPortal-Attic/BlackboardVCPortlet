package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
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
public class SessionWSDaoIT extends AbstractWSIT {
	
	@Autowired
	private SessionWSDao dao;
	
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
}
