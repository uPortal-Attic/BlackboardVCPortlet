package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import javax.activation.DataHandler;

import com.elluminate.sas.BlackboardPresentationResponse;

public interface PresentationWSDao {
	
	//create
	/**
	 * Note this first uploads the presentation then links it to the session
	 * @param sessionId
	 * @param creatorId
	 * @param filename
	 * @param description
	 * @param content
	 * @return 
	 */
	public BlackboardPresentationResponse uploadPresentation(Long sessionId, String creatorId, String filename, String description, DataHandler data);
	public BlackboardPresentationResponse uploadPresentation(String creatorId, String filename, String description, DataHandler data);
	public boolean linkPresentationToSession(Long sessionId, Long presentationId);

	//read
	public List getSessionPresentations(Long sessionId);
	public List getRepositoryPresentations(String creatorId, Long presentationId, String description);
	
	//delete
	public boolean deletePresentation(Long presentationId);
	public boolean deleteSessionPresenation(Long sessionId, Long presenationId);
	

	
}
