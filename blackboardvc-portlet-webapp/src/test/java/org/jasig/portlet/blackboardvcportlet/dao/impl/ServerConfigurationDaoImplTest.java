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
