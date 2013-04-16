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
	public BlackboardPresentationResponse uploadPresentation(long sessionId, String creatorId, String filename, String description, DataHandler data);
	public BlackboardPresentationResponse uploadPresentation(String creatorId, String filename, String description, DataHandler data);
	public boolean linkPresentationToSession(long sessionId, long presentationId);

	//read
	public List<BlackboardPresentationResponse> getSessionPresentations(long sessionId);
	/**
	 * Must specify at least one piece of criteria, and can have multiple
	 * @param creatorId
	 * @param presentationId Long instead of long so we can pass in null to the search
	 * @param description
	 * @return
	 */
	public List<BlackboardPresentationResponse> getRepositoryPresentations(String creatorId, Long presentationId, String description);
	
	//delete
	public boolean deletePresentation(long presentationId);
	public boolean deleteSessionPresenation(long sessionId, long presenationId);
	

	
}
