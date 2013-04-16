package org.jasig.portlet.blackboardvcportlet.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.jasig.portlet.blackboardvcportlet.dao.ConferenceUserDao;
import org.jasig.portlet.blackboardvcportlet.dao.MultimediaDao;
import org.jasig.portlet.blackboardvcportlet.data.Multimedia;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardMultimediaResponse;
import com.google.common.collect.ImmutableMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jpaTestContext.xml")
public class ConferenceUserDaoImplTest extends BaseJpaDaoTest {
    @Autowired
    private ConferenceUserDao conferenceUserDao;
    
    @Autowired
    private MultimediaDao multimediaDao;
    
    @Test
    public void testEmptyQueries() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.getUser(1);
                assertNull(user);
                
                return null;
            }
        });
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.getUser("user@example.com");
                assertNull(user);
                
                return null;
            }
        });
        
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final Map<String, String> attrs = ImmutableMap.of("EPPN", "user@example.com", "SPVI", "012332412");
                final Set<ConferenceUser> users = conferenceUserDao.findAllMatchingUsers("user@example.com", attrs);
                assertEquals(0, users.size());
                
                return null;
            }
        });
        
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.getUser(1);
                final Set<Session> chairedSessions = conferenceUserDao.getChairedSessionsForUser(user);
                assertNull(chairedSessions);
                
                return null;
            }
        });
        
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.getUser(1);
                final Set<Session> participatingSessions = conferenceUserDao.getNonChairedSessionsForUser(user);
                assertNull(participatingSessions);
                
                return null;
            }
        });
    }
    
    @Test
    public void testMultimedias() throws Exception {
    	//Create user
    	this.execute(new Callable<Object>() {
    		@Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.createUser("user@example.com", "Example User");

                assertNotNull(user);
                assertEquals("user@example.com", user.getEmail());
                assertEquals("Example User", user.getDisplayName());
                assertEquals(0, user.getAttributes().size());
                
                return null;
            }
    	});
    	
    	//add multimedia
    	this.execute(new Callable<Object>() {
    		@Override
    		public Object call() {
    			final BlackboardMultimediaResponse response = new BlackboardMultimediaResponse();
                response.setCreatorId("user@example.com");
                response.setDescription("super sweet media");
                response.setMultimediaId(183838);
                response.setSize(1024);
                
                final Multimedia mm = multimediaDao.createMultimedia(response, "aliens_exist.pdf");
                assertNotNull(mm);
                
                return null;
    		}
    	});
    	
    	//Assert that the creator id matched to the conference user
    	this.execute(new Callable<Object>() {
    		@Override
    		public Object call() {
    			final ConferenceUser user = conferenceUserDao.getUser("user@example.com");
                Set<Multimedia> mms = conferenceUserDao.getMultimediasForUser(user);
                assertEquals(1,mms.size());
                final Multimedia mm = DataAccessUtils.singleResult(mms);
                assertNotNull(mm);
                assertEquals(mm.getFilename(),"aliens_exist.pdf");
                return null;
    		}
    	});
    }
    
    @Test
    public void testMultipleEmails() throws Exception {
        //Create user with no attributes
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.createUser("user@example.com", "Example User");

                assertNotNull(user);
                assertEquals("user@example.com", user.getEmail());
                assertEquals("Example User", user.getDisplayName());
                assertEquals(0, user.getAttributes().size());
                
                return null;
            }
        });
        
        //Create user with two attributes
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.createUser("example.user@example.com", "Example User");
                user.getAttributes().put("EPPN", "user@example.com");
                user.getAttributes().put("SPVI", "1234567");
                conferenceUserDao.updateUser(user);

                assertNotNull(user);
                assertEquals("example.user@example.com", user.getEmail());
                assertEquals("Example User", user.getDisplayName());
                assertEquals(2, user.getAttributes().size());
                
                return null;
            }
        });
        
        //Add attributes to first user
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.getUser("user@example.com");
                user.getAttributes().put("EPPN", "user@example.com");
                user.getAttributes().put("SPVI", "1234567");
                conferenceUserDao.updateUser(user);

                assertNotNull(user);
                assertEquals("user@example.com", user.getEmail());
                assertEquals("Example User", user.getDisplayName());
                assertEquals(2, user.getAttributes().size());
                
                return null;
            }
        });
        
        //Add user 3
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final ConferenceUser user = conferenceUserDao.createUser("admin@example.com", "Example Admin");
                user.getAttributes().put("EPPN", "admin@example.com");
                user.getAttributes().put("SPVI", "789456");
                conferenceUserDao.updateUser(user);

                assertNotNull(user);
                assertEquals("admin@example.com", user.getEmail());
                assertEquals("Example Admin", user.getDisplayName());
                assertEquals(2, user.getAttributes().size());
                
                return null;
            }
        });
        
        //Find both users
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final Map<String, String> attrs = ImmutableMap.of("EPPN", "user@example.com", "SPVI", "1234567");
                final Set<ConferenceUser> users = conferenceUserDao.findAllMatchingUsers("user@example.com", attrs);
                assertEquals(2, users.size());
                
                return null;
            }
        });
    }
}
