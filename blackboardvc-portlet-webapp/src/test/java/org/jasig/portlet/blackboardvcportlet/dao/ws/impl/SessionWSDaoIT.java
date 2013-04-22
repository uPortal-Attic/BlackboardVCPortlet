package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/integration-test-applicationContext.xml")
public class SessionWSDaoIT extends SessionWSDaoTestBase {
	
	@Override
	@Test
	public void createSessionTest() {
		super.createSessionTest();
	}
	
	@Override
	@Test
	public void updateSessionTest() throws Exception {
		super.updateSessionTest();
	}
	
	@Override
	@Test
	public void buildSessionGuestUrlTest() throws Exception {
		super.buildSessionGuestUrlTest();
	}
	
	/**
	 * This test case assumes the creator is initially added as a chair (moderator)
	 */
	@Override
	@Test
	public void buildSessionUrlTest() throws Exception {
		super.buildSessionUrlTest();
	}
	
	@Override
	@Test
	public void getSessionsByEmailAddressTest() throws Exception {
		super.getSessionsByEmailAddressTest();
	}
	
	@Override
	@Test
	public void getSessionsBySessionIdTest() throws Exception {
		super.getSessionsBySessionIdTest();
	}
	
	@Override
	@Test
	public void clearSessionChairList() throws Exception {
		super.clearSessionChairList();
	}
	
	@Override
	@Test
	public void clearSessionNonChairList() throws Exception {
		super.clearSessionNonChairList();
	}
}
