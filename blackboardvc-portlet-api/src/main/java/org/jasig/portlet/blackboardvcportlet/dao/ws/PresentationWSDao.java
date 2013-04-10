package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

public interface PresentationWSDao {
	
	//create
	/**
	 * Note this first uploads the presentation then links it to the session
	 * @param sessionId
	 * @param creatorId
	 * @param filename
	 * @param description
	 * @param content
	 */
	public void uploadPresenation(int sessionId, String creatorId, String filename, String description, Object content);

	//read
	public List getSessionPresentations(int sessionId);
	public List getRepositoryPresentations(int creatorId, int presentationId, String description);
	
	//delete
	public void deletePresentation(int presentationId);
	public void deleteSessionPresenation(int sessionId, int presenationId);
}
