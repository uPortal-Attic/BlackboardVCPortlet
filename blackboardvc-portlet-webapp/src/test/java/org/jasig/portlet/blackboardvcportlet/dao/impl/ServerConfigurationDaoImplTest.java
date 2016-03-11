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
package org.jasig.portlet.blackboardvcportlet.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Callable;

import org.jasig.portlet.blackboardvcportlet.dao.ServerConfigurationDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerConfiguration;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardServerConfigurationResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jpaTestContext.xml")
public class ServerConfigurationDaoImplTest extends BaseJpaDaoTest {
    @Autowired
    private ServerConfigurationDao serverConfigurationDao;
    
    @Test
    public void testEmptyQueries() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ServerConfiguration serverConfiguration = serverConfigurationDao.getServerConfiguration();
                assertNull(serverConfiguration);
                
                return null;
            }
        });
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                serverConfigurationDao.deleteServerConfiguration();
                return null;
            }
        });
    }
    
    @Test
    public void testCreateUpdate() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final BlackboardServerConfigurationResponse configurationResponse = new BlackboardServerConfigurationResponse();
                configurationResponse.setBoundaryTime(30);
                configurationResponse.setMaxAvailableTalkers(6);
                configurationResponse.setMaxAvailableCameras(6);
                configurationResponse.setRaiseHandOnEnter(false);
                configurationResponse.setMayUseTelephony(false);
                configurationResponse.setMayUseSecureSignOn(false);
                configurationResponse.setMustReserveSeats(false);
                configurationResponse.setTimeZone("Mountain (North America/Canada, GMT -06:00)");
                
                final ServerConfiguration serverConfiguration = serverConfigurationDao.createOrUpdateConfiguration(configurationResponse);
                assertNotNull(serverConfiguration);
                
                assertEquals(DateTimeZone.forOffsetHours(-6), serverConfiguration.getTimezone());
                
                return null;
            }
        });
        

        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final BlackboardServerConfigurationResponse configurationResponse = new BlackboardServerConfigurationResponse();
                configurationResponse.setBoundaryTime(30);
                configurationResponse.setMaxAvailableTalkers(6);
                configurationResponse.setMaxAvailableCameras(6);
                configurationResponse.setRaiseHandOnEnter(false);
                configurationResponse.setMayUseTelephony(false);
                configurationResponse.setMayUseSecureSignOn(false);
                configurationResponse.setMustReserveSeats(false);
                configurationResponse.setTimeZone("Mountain (North America/Canada, GMT -07:00)");
                
                final ServerConfiguration serverConfiguration = serverConfigurationDao.createOrUpdateConfiguration(configurationResponse);
                assertNotNull(serverConfiguration);
                
                assertEquals(DateTimeZone.forOffsetHours(-7), serverConfiguration.getTimezone());
                
                return null;
            }
        });
    }
}
