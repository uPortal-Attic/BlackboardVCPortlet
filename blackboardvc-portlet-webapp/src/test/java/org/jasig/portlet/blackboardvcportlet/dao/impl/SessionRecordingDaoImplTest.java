package org.jasig.portlet.blackboardvcportlet.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Set;
import java.util.concurrent.Callable;

import org.jasig.portlet.blackboardvcportlet.dao.BlackboardSessionDao;
import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardSession;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.RecordingLongResponse;
import com.elluminate.sas.SessionResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jpaTestContext.xml")
public class SessionRecordingDaoImplTest extends BaseJpaDaoTest {
    @Autowired
    private SessionRecordingDao sessionRecordingDao;
    @Autowired
    private BlackboardSessionDao blackboardSessionDao;
    
    @Test
    public void testEmptyQueries() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final Set<SessionRecording> allRecordings = sessionRecordingDao.getAllRecordings();
                assertEquals(0, allRecordings.size());
                
                return null;
            }
        });
    }
    
    @Test
    public void testCreateNoSession() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final RecordingLongResponse recordingLongResponse = new RecordingLongResponse();
                
                recordingLongResponse.setCreationDate(1364566600000l);
                recordingLongResponse.setRecordingId(1);
                recordingLongResponse.setRecordingSize(12345);
                recordingLongResponse.setRecordingURL("http://www.example.com/recording/1");
                recordingLongResponse.setRoomEndDate(1364567400000l);
                recordingLongResponse.setRoomName("Test Room");
                recordingLongResponse.setRoomStartDate(1364566500000l);
                recordingLongResponse.setSecureSignOn(false);
                recordingLongResponse.setSessionId(1);
                
                try {
                    sessionRecordingDao.createOrUpdateRecording(recordingLongResponse);
                    fail("Should have failed with IllegalArgumentException");
                }
                catch (IllegalArgumentException e) {
                    //Expected
                }
                
                return null;
            }
        });
    }
    
    @Test
    public void testCreateWithSession() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final SessionResponse sessionResponse = new SessionResponse();
                sessionResponse.setSessionId(1);
                sessionResponse.setSessionName("Test Session");
                sessionResponse.setStartTime(1364566500000l);
                sessionResponse.setEndTime(1364567400000l);
                sessionResponse.setCreatorId("admin@example.com");
                sessionResponse.setBoundaryTime(30);
                sessionResponse.setAccessType(2);
                sessionResponse.setRecordings(false);
                sessionResponse.setChairList("admin@example.com,dalquist@example.com");
                sessionResponse.setNonChairList("levett@example.com");
                sessionResponse.setOpenChair(false);
                sessionResponse.setPermissionsOn(true);
                sessionResponse.setMustBeSupervised(true);
                sessionResponse.setRecordingModeType(3);
                sessionResponse.setMaxTalkers(6);
                sessionResponse.setMaxCameras(6);
                sessionResponse.setRaiseHandOnEnter(false);
                sessionResponse.setReserveSeats(0);
                sessionResponse.setSecureSignOn(false);
                sessionResponse.setVersionId(111);
                sessionResponse.setAllowInSessionInvites(true);
                sessionResponse.setHideParticipantNames(true);
                
                final BlackboardSession session = blackboardSessionDao.createSession(sessionResponse, "http://www.example.com/session");
                assertNotNull(session);
                
                return null;
            }
        });
        
        //Create rec 2
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final RecordingLongResponse recordingLongResponse = new RecordingLongResponse();
                
                recordingLongResponse.setCreationDate(1364566600000l);
                recordingLongResponse.setRecordingId(1);
                recordingLongResponse.setRecordingSize(12345);
                recordingLongResponse.setRecordingURL("http://www.example.com/recording/1");
                recordingLongResponse.setRoomEndDate(1364567400000l);
                recordingLongResponse.setRoomName("Test Room");
                recordingLongResponse.setRoomStartDate(1364566500000l);
                recordingLongResponse.setSecureSignOn(false);
                recordingLongResponse.setSessionId(1);
                
                final SessionRecording recording = sessionRecordingDao.createOrUpdateRecording(recordingLongResponse);
                assertNotNull(recording);
                assertEquals("2013-03-29T14:16:40.000Z", recording.getCreationDate().toString());
                assertEquals(false, recording.isSecureSignOn());
                
                return null;
            }
        });
        
        //Update rec 1
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final RecordingLongResponse recordingLongResponse = new RecordingLongResponse();
                
                recordingLongResponse.setCreationDate(1364566600000l);
                recordingLongResponse.setRecordingId(1);
                recordingLongResponse.setRecordingSize(12345);
                recordingLongResponse.setRecordingURL("http://www.example.com/recording/1");
                recordingLongResponse.setRoomEndDate(1364567400000l);
                recordingLongResponse.setRoomName("Test Room");
                recordingLongResponse.setRoomStartDate(1364566500000l);
                recordingLongResponse.setSecureSignOn(true);
                recordingLongResponse.setSessionId(1);
                
                final SessionRecording recording = sessionRecordingDao.createOrUpdateRecording(recordingLongResponse);
                assertNotNull(recording);
                assertEquals("2013-03-29T14:16:40.000Z", recording.getCreationDate().toString());
                assertEquals(true, recording.isSecureSignOn());
                
                return null;
            }
        });
        
        //Create rec 2
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final RecordingLongResponse recordingLongResponse = new RecordingLongResponse();
                
                recordingLongResponse.setCreationDate(1364566600000l);
                recordingLongResponse.setRecordingId(2);
                recordingLongResponse.setRecordingSize(12345);
                recordingLongResponse.setRecordingURL("http://www.example.com/recording/2");
                recordingLongResponse.setRoomEndDate(1364567400000l);
                recordingLongResponse.setRoomName("Test Room");
                recordingLongResponse.setRoomStartDate(1364566500000l);
                recordingLongResponse.setSecureSignOn(true);
                recordingLongResponse.setSessionId(1);
                
                final SessionRecording recording = sessionRecordingDao.createOrUpdateRecording(recordingLongResponse);
                assertNotNull(recording);
                assertEquals("2013-03-29T14:16:40.000Z", recording.getCreationDate().toString());
                assertEquals(true, recording.isSecureSignOn());
                
                return null;
            }
        });
        
        //Create rec 3
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final RecordingLongResponse recordingLongResponse = new RecordingLongResponse();
                
                recordingLongResponse.setCreationDate(1364566600000l);
                recordingLongResponse.setRecordingId(3);
                recordingLongResponse.setRecordingSize(12345);
                recordingLongResponse.setRecordingURL("http://www.example.com/recording/3");
                recordingLongResponse.setRoomEndDate(1364567400000l);
                recordingLongResponse.setRoomName("Test Room");
                recordingLongResponse.setRoomStartDate(1364566500000l);
                recordingLongResponse.setSecureSignOn(true);
                recordingLongResponse.setSessionId(1);
                
                final SessionRecording recording = sessionRecordingDao.createOrUpdateRecording(recordingLongResponse);
                assertNotNull(recording);
                assertEquals("2013-03-29T14:16:40.000Z", recording.getCreationDate().toString());
                assertEquals(true, recording.isSecureSignOn());
                
                return null;
            }
        });

        //Verify 3 rec
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final Set<SessionRecording> allRecordings = sessionRecordingDao.getAllRecordings();
                assertEquals(3, allRecordings.size());
                
                return null;
            }
        });
        
        //Delete rec 1 & 3
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                
                final int count = sessionRecordingDao.deleteRecordings(1, 3);
                
                assertEquals(2, count);
                
                return null;
            }
        });

        //Verify 1 rec
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final Set<SessionRecording> allRecordings = sessionRecordingDao.getAllRecordings();
                assertEquals(1, allRecordings.size());
                
                return null;
            }
        });
    }
}
