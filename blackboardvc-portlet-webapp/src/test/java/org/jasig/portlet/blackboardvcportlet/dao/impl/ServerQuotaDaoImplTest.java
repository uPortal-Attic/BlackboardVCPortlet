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

import org.jasig.portlet.blackboardvcportlet.dao.ServerQuotaDao;
import org.jasig.portlet.blackboardvcportlet.data.ServerQuota;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardServerQuotasResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jpaTestContext.xml")
public class ServerQuotaDaoImplTest extends BaseJpaDaoTest {
    @Autowired
    private ServerQuotaDao serverQuotaDao;
    
    @Test
    public void testEmptyQueries() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ServerQuota serverQuota = serverQuotaDao.getServerQuota();
                assertNull(serverQuota);
                
                return null;
            }
        });
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                serverQuotaDao.deleteServerQuota();
                return null;
            }
        });
    }
    
    @Test
    public void testCreateUpdate() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final BlackboardServerQuotasResponse quotasResponse = new BlackboardServerQuotasResponse();
                quotasResponse.setDiskQuota(10737418240l);
                quotasResponse.setDiskQuotaAvailable(1073741824);
                quotasResponse.setSessionQuota(1024);
                quotasResponse.setSessionQuotaAvailable(1012);
                
                final ServerQuota serverQuota = serverQuotaDao.createOrUpdateQuota(quotasResponse);
                assertNotNull(serverQuota);
                
                assertEquals(10737418240l, serverQuota.getDiskQuota());
                
                return null;
            }
        });
        

        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final BlackboardServerQuotasResponse quotasResponse = new BlackboardServerQuotasResponse();
                quotasResponse.setDiskQuota(1073741824);
                quotasResponse.setDiskQuotaAvailable(1073741824);
                quotasResponse.setSessionQuota(1024);
                quotasResponse.setSessionQuotaAvailable(1012);
                
                final ServerQuota serverQuota = serverQuotaDao.createOrUpdateQuota(quotasResponse);
                assertNotNull(serverQuota);
                
                assertEquals(1073741824, serverQuota.getDiskQuota());
                
                return null;
            }
        });
    }
}
