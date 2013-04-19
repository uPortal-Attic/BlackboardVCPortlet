package org.jasig.portlet.blackboardvcportlet.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;
import java.util.concurrent.Callable;

import org.jasig.portlet.blackboardvcportlet.dao.PresentationDao;
import org.jasig.portlet.blackboardvcportlet.data.Presentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elluminate.sas.BlackboardPresentationResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jpaTestContext.xml")
public class PresentationDaoImplTest extends BaseJpaDaoTest {
	
	@Autowired
	private PresentationDao dao;
	
	@Test
    public void testEmptyQueries() throws Exception {
        this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final Set<Presentation> presentation = dao.getAllPresentations();
                assertEquals(0, presentation.size());
                
                return null;
            }
        });
    }
	
	@Test
	public void testCreatePresentation() {
		this.execute(new Callable<Object>() {
            @Override
            public Object call() {
                final BlackboardPresentationResponse response = new BlackboardPresentationResponse();
                response.setCreatorId("test@example.com");
                response.setDescription("super sweet media");
                response.setPresentationId(183838);
                response.setSize(1024);
                
                final Presentation pres = dao.createPresentation(response, "aliens_exist.pdf");
                assertNotNull(pres);
                
                assertEquals(1024, pres.getSize());
                assertEquals(183838, pres.getBbPresentationId());
                
                Presentation pullFromDB = dao.getPresentationByBlackboardId(pres.getBbPresentationId());
                assertNotNull(pullFromDB);
                
                return null;
            }
        });
	}
	
	@Test
	public void testDelete() {
		this.execute(new Callable<Object>() {
            @Override
            public Object call() {
            	final BlackboardPresentationResponse response = new BlackboardPresentationResponse();
                response.setCreatorId("test@example.com");
                response.setDescription("super sweet media");
                response.setPresentationId(183838);
                response.setSize(1024);
                
                final Presentation pres = dao.createPresentation(response, "aliens_exist.pdf");
                assertNotNull(pres);
                
                dao.deletePresentation(pres);
                
                Presentation shouldBeNull = dao.getPresentationByBlackboardId(pres.getBbPresentationId());
                assertNull(shouldBeNull);
                return null;
            }
        });
	}
}
