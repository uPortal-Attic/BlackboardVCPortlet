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
