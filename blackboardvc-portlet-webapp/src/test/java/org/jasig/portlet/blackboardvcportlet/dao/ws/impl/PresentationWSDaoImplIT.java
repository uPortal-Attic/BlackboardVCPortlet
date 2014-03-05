package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/integration-test-applicationContext.xml")
public class PresentationWSDaoImplIT extends PresentationWSDaoImplTestBase {

	@Override
	@Test
	public void uploadPresentationTest() throws Exception {
		super.uploadPresentationTest();
	}

	@Override
	@Test
	public void linkPresentationToSessionTest() throws Exception {
		super.linkPresentationToSessionTest();
	}

	@Override
	@Test
	public void getSessionPresentations() throws Exception {
		super.getSessionPresentations();
	}

	@Override
	@Test
	public void getRepositoryPresentationsTest() throws Exception {
		super.getRepositoryPresentationsTest();
	}

	@Override
	@Test
	public void deletePresentation() throws Exception {
		super.deletePresentation();
	}

	@Override
	@Test
	public void deleteSessionPresenation() throws Exception {
		super.deleteSessionPresenation();
	}
}
