/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
