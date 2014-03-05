package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;
import org.jasig.portlet.blackboardvcportlet.security.SecurityExpressionEvaluator;
import org.jasig.springframework.mockito.MockitoFactoryBean;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;

import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSessionTelephonyResponse;

public class SessionWSDaoTestBase  extends AbstractWSIT {
	
	@Autowired
	SessionWSDao dao;
	
	@Autowired
	SecurityExpressionEvaluator security;
	
	@SuppressWarnings("unchecked")
	@Before
	public void before() throws Exception {
		MockitoFactoryBean.resetAllMocks();
		when(security.authorize(any(String.class))).thenReturn(true);
		when(security.authorize(any(String.class),any(Map.class))).thenReturn(true);
		form = buildSession();
		user = buildUser();
		session = dao.createSession(user, form);
	}
	
	@After
	public void after() throws Exception {
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, null, user.getUniqueId(), null, null, null);
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
		
		assertEquals(session.getCreatorId(), user.getUniqueId());
	}
	
	@Test
	public void createSessionTelephony() {
		SessionTelephony tel = buildSessionTelephony();
		BlackboardSessionTelephonyResponse response = dao.createSessionTelephony(session.getSessionId(), tel);
		assertNotNull(response);
		assertEquals(response.getChairPhone(),tel.getChairPhone());
		assertEquals(response.getSessionPIN(), tel.getSessionPIN());
	}
	
	@Test
	public void updateSessionTest() throws Exception {
		form.setNewSession(false);
		form.setSessionId(session.getSessionId());
		
		//go from 3 to 4 (four hour meeting)
		form.setEndTime((new DateTime()).plusHours(5).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0));
		BlackboardSessionResponse updateSession = dao.updateSession(session.getSessionId(), form);
		assertNotNull(updateSession);
		assertEquals(form.getSessionId(), updateSession.getSessionId());
	}
	
	@Test
	public void buildSessionGuestUrlTest() throws Exception {
		String url = dao.buildGuestSessionUrl(session.getSessionId());
		assertNotNull(url);
		
	}
	
	/**
	 * This test case assumes the creator is initially added as a chair (moderator)
	 * @throws Exception 
	 */
	@Test
	public void buildSessionUrlTest() throws Exception {
		String url = dao.buildSessionUrl(session.getSessionId(), user);
		assertNotNull(url);
		
	}
	
	@Test
	public void getSessionsByEmailAddressTest() throws Exception {
		
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, null, session.getCreatorId(), null, null, null);
		assertNotNull(sessions);
		assertEquals(DataAccessUtils.singleResult(sessions).getSessionId(), session.getSessionId());
	}
	
	@Test
	public void getSessionsBySessionIdTest() throws Exception {
		
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, session.getSessionId(), null, null, null, null);
		assertNotNull(sessions);
		assertEquals(DataAccessUtils.singleResult(sessions).getSessionId(), session.getSessionId());
	}
	
	@Test
	public void clearSessionChairList() throws Exception {
		assertTrue(dao.clearSessionChairList(session.getSessionId()));
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, session.getSessionId(), null, null, null, null);
		BlackboardSessionResponse singleSession = DataAccessUtils.singleResult(sessions);
		assertTrue(singleSession.getChairList().isEmpty());
		assertFalse(singleSession.getNonChairList().isEmpty());
	}
	
	@Test
	public void clearSessionNonChairList() throws Exception {
		assertTrue(dao.clearSessionNonChairList(session.getSessionId()));
		List<BlackboardSessionResponse> sessions = dao.getSessions(null, null, session.getSessionId(), null, null, null, null);
		BlackboardSessionResponse singleSession = DataAccessUtils.singleResult(sessions);
		assertEquals(0,singleSession.getNonChairList().length());
		assertFalse(singleSession.getChairList().isEmpty());
	}
}
