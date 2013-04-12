package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import org.hibernate.annotations.Any;
import org.jasig.portlet.blackboardvcportlet.dao.ws.GlobalSettingsWSDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardServerConfigurationResponse;
import com.elluminate.sas.BlackboardServerQuotasResponse;
import com.elluminate.sas.BlackboardServerVersionResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-applicationContext.xml")
public class GlobalSettingsWSDaoImplIT {
	private static final Logger logger = LoggerFactory.getLogger(GlobalSettingsWSDaoImplIT.class);
	
	@Autowired
	private GlobalSettingsWSDao dao;

	@Test
	public void getServerConfigurationTest() {
		BlackboardServerConfigurationResponse response = dao.getServerConfiguration();
		assertNotNull(response);
		assertEquals(response.getBoundaryTime(),30);
		assertEquals(response.getMaxAvailableCameras(),6);
		assertEquals(response.getMaxAvailableTalkers(),6);
		assertEquals(response.getTimeZone(),"Mountain (North America/Canada, GMT -06:00)");
	}
	
	@Test
	public void getServerQuotaTest() {
		BlackboardServerQuotasResponse response = dao.getServerQuota();
		assertNotNull(response);
		assertEquals(response.getDiskQuota(),1073741824);
		assertEquals(response.getDiskQuotaAvailable(),1073741806);
		assertEquals(response.getSessionQuota(),1024);
		assertEquals(response.getSessionQuotaAvailable(),1006);
	}
	
	@Test
	public void getServerVersionsTest() {
		BlackboardServerVersionResponse serverVersions = dao.getServerVersions();
		assertNotNull(serverVersions);
		long versionId = serverVersions.getVersionId();
		int versionMaxFilmersLimit = serverVersions.getVersionMaxFilmersLimit();
		int versionMaxTalkersLimit = serverVersions.getVersionMaxTalkersLimit();
		String versionName = serverVersions.getVersionName();
		assertEquals(versionId,111);
		assertEquals(versionMaxFilmersLimit,6);
		assertEquals(versionMaxTalkersLimit, 6);
		assertEquals(versionName, "10.0");
	}
	
	@Test
	public void setApiCallbackUrlTest() {
		boolean setApiCallbackUrl = dao.setApiCallbackUrl("THISWILLBREAKTHINGSSOBECAREFUL");
		assertTrue(setApiCallbackUrl);
	}
}
