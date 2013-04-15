package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.jasig.portlet.blackboardvcportlet.dao.ws.MultimediaWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardMultimediaResponse;
import com.elluminate.sas.BlackboardSessionResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-applicationContext.xml")
public class MultimediaWSDaoIT extends AbstractWSIT {

	@Autowired
	private MultimediaWSDao dao;
	
	@Autowired
	private SessionWSDao sessionDao;
	
	private List <Long> multimedias = new ArrayList<Long>() ;
	
	@Before
	public void before() {
		form = buildSession();
		user = buildUser();
		session = sessionDao.createSession(user, form);
	}
	
	@After
	public void after() {
		List<BlackboardSessionResponse> sessions = sessionDao.getSessions(null, null, null, user.getEmail(), null, null, null);
		for(BlackboardSessionResponse session : sessions ) {
			List<BlackboardMultimediaResponse> repositoryMultimedias = dao.getSessionRepositoryMultimedias(session.getSessionId());
			for(BlackboardMultimediaResponse multimedia : repositoryMultimedias) {
				dao.removeSessionMultimedia(session.getSessionId(),multimedia.getMultimediaId());
			}
			sessionDao.deleteSession(session.getSessionId());			
		}
		
		for(Long multimediaId : multimedias) {
			dao.removeRepositoryMultimedia(multimediaId);
		}
		
	}
	
	@Test
	public void getRepositoryMultimediasTest () {
		List<BlackboardMultimediaResponse> repositoryMultimedias = dao.getRepositoryMultimedias(user.getEmail(), null, null);
		assertEquals(repositoryMultimedias.size(),0);
	}

	@Test
	public void getSessionRepositoryMultimediasTest() throws IOException {
		List<BlackboardMultimediaResponse> sessionRepositoryMultimedias = dao.getSessionRepositoryMultimedias(session.getSessionId());
		assertTrue(sessionRepositoryMultimedias.size() == 0);
	}
	
	@Test
	public void uploadRepositoryMultimediaTest() throws IOException {
		BlackboardMultimediaResponse uploadRepositoryMultimedia = createRepoMultimedia();
		assertNotNull(uploadRepositoryMultimedia);
	}
	
	@Test
	public void createSessionMultimediaTest() throws IOException {
		InputStream is = new ByteArrayInputStream("TEST2".getBytes());
        ByteArrayDataSource rawData = new ByteArrayDataSource(is,"video/mpeg");
		DataHandler dataHandler = new DataHandler(rawData);
		BlackboardMultimediaResponse createSessionMultimedia = dao.createSessionMultimedia(session.getSessionId(), user.getEmail(), "test.mpeg", "aliens",dataHandler);
		
		multimedias.add(createSessionMultimedia.getMultimediaId());
		
		List<BlackboardMultimediaResponse> repositoryMultimedias = dao.getSessionRepositoryMultimedias(session.getSessionId());
		assertNotNull(repositoryMultimedias);
		assertTrue(repositoryMultimedias.size() == 1);
	}
	
	@Test
	public void linkSessionToMultimediaTest() throws IOException {
		BlackboardMultimediaResponse uploadRepositoryMultimedia = createRepoMultimedia();
		assertNotNull(uploadRepositoryMultimedia);
		
		boolean linkSessionToMultimedia = dao.linkSessionToMultimedia(session.getSessionId(), uploadRepositoryMultimedia.getMultimediaId());
		
		assertTrue(linkSessionToMultimedia);
		
		List<BlackboardMultimediaResponse> repositoryMultimedias = dao.getSessionRepositoryMultimedias(session.getSessionId());
		assertNotNull(repositoryMultimedias);
		assertTrue(repositoryMultimedias.size() == 1);
		
	}
	
	@Test
	public void removeRepositoryMultimediaTest() throws IOException {
		BlackboardMultimediaResponse createRepoMultimedia = createRepoMultimedia();
		assertNotNull(createRepoMultimedia);
		
		boolean removeRepositoryMultimedia = dao.removeRepositoryMultimedia(createRepoMultimedia.getMultimediaId());
		
		assertTrue(removeRepositoryMultimedia);
	}
	
	@Test
	public void removeSessionMultimediaTest() throws IOException {
		BlackboardMultimediaResponse multimedia = createRepoMultimedia();
		assertNotNull(multimedia);
		
		assertTrue(dao.linkSessionToMultimedia(session.getSessionId(), multimedia.getMultimediaId()));
		
		dao.removeSessionMultimedia(session.getSessionId(), multimedia.getMultimediaId());
		
	}
	
	private BlackboardMultimediaResponse createRepoMultimedia() throws IOException {
		InputStream is = new ByteArrayInputStream("fdsdfsfsdadsfasfda".getBytes());
        ByteArrayDataSource rawData = new ByteArrayDataSource(is,"video/mpeg");
		DataHandler dataHandler = new DataHandler(rawData);

        BlackboardMultimediaResponse uploadRepositoryMultimedia = dao.uploadRepositoryMultimedia(user.getEmail(), "test.mpeg", "aliens",dataHandler);
        multimedias.add(uploadRepositoryMultimedia.getMultimediaId());
        return uploadRepositoryMultimedia;
	}

}
