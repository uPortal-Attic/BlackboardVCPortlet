package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/integration-test-applicationContext.xml")
public class MultimediaWSDaoIT extends MultimediaWSDaoTestBase {
	
	@Test
	public void getRepositoryMultimediasTest () throws Exception {
		super.getRepositoryMultimediasTest();
	}
    

	@Test
	public void getSessionRepositoryMultimediasTest()  throws Exception {
		super.getSessionRepositoryMultimediasTest();
	}
	
	@Test
	public void uploadRepositoryMultimediaTest()  throws Exception {
		super.uploadRepositoryMultimediaTest();
	}
	
	@Test
	public void createSessionMultimediaTest() throws Exception {
		super.createSessionMultimediaTest();
	}
	
	@Test
	public void linkSessionToMultimediaTest() throws Exception {
		super.linkSessionToMultimediaTest();
		
	}
	
	@Test
	public void removeRepositoryMultimediaTest() throws Exception {
		super.removeRepositoryMultimediaTest();
	}
	
	@Test
	public void removeSessionMultimediaTest() throws Exception {
		super.removeSessionMultimediaTest();
		
	}
}
