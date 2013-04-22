package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jasig.portlet.blackboardvcportlet.dao.ws.GlobalSettingsWSDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.elluminate.sas.BlackboardServerConfigurationResponse;
import com.elluminate.sas.BlackboardServerQuotasResponse;
import com.elluminate.sas.BlackboardServerVersionResponse;

public class GlobalSettingsWSDaoImplTestBase {
private GlobalSettingsWSDao dao;
	
	@Autowired
	public void setGlobalSettingWSDao (GlobalSettingsWSDao dao) {
		this.dao = dao;
	}

	@Test
	public void getServerConfigurationTest() throws Exception {
		BlackboardServerConfigurationResponse response = dao.getServerConfiguration();
		assertNotNull(response);
		assertEquals(response.getBoundaryTime(),30);
		assertEquals(response.getMaxAvailableCameras(),6);
		assertEquals(response.getMaxAvailableTalkers(),6);
		assertEquals(response.getTimeZone(),"Mountain (North America/Canada, GMT -06:00)");
	}
	
	@Test
	public void getServerQuotaTest() throws Exception {
		BlackboardServerQuotasResponse response = dao.getServerQuota();
		assertNotNull(response);
		assertEquals(1073741824, response.getDiskQuota());
		assertEquals(1062919645, response.getDiskQuotaAvailable());
		assertEquals(1024, response.getSessionQuota());
		assertEquals(996, response.getSessionQuotaAvailable());
	}
	
	@Test
	public void getServerVersionsTest() throws Exception {
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
	public void setApiCallbackUrlTest() throws Exception {
		boolean setApiCallbackUrl = dao.setApiCallbackUrl("THISWILLBREAKTHINGSSOBECAREFUL");
		assertTrue(setApiCallbackUrl);
	}
}
