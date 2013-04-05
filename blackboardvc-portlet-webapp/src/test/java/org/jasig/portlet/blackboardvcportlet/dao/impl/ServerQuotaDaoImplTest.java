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
