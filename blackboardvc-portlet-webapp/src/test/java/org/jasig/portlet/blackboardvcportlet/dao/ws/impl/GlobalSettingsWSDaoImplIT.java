package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/integration-test-applicationContext.xml")
public class GlobalSettingsWSDaoImplIT extends GlobalSettingsWSDaoImplTestBase {
	
	@Test
    @Override
	public void getServerConfigurationTest() throws Exception {
		super.getServerConfigurationTest();
	}
	
	@Test
    @Override
	public void getServerQuotaTest() throws Exception {
		super.getServerQuotaTest();
	}
	
	@Test
    @Override
	public void getServerVersionsTest() throws Exception {
		super.getServerVersionsTest();
	}
	
	@Test
    @Override
	public void setApiCallbackUrlTest() throws Exception {
		super.setApiCallbackUrlTest();
	}
}
