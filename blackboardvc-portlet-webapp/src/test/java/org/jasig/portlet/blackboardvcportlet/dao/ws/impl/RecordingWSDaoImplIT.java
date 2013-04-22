package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/integration-test-applicationContext.xml")
public class RecordingWSDaoImplIT extends RecordingWSDaoImplTestBase {
	
	@Override
	@Test
	public void getRecordingLongTest() throws Exception {
		super.getRecordingLongTest();
	}

	@Override
	@Test
	public void getRecordingShortTest() throws Exception{
		super.getRecordingShortTest();
		
	}

	//The following methods don't work because that recording id is a fake and we don't have a test case.
	@Override
	@Test
	public void removeRecordingTest() throws Exception {
		super.removeRecordingTest();
	}

	@Override
	@Test
	public void buildRecordingUrlTest() throws Exception {
		super.buildRecordingUrlTest();
	}

	@Override
	@Test
	public void updateRecordingSecureSignOnTest() throws Exception {
		super.updateRecordingSecureSignOnTest();
	}

}
