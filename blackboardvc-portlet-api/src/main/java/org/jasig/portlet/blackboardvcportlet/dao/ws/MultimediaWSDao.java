package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

public interface MultimediaWSDao {
	//read
	public List getRepositoryMultimedias(int creatorId, int multimediaId, String description);
	public List getSessionRepositoryMultimedias(int sessionId);
	
	//create
	/**
	 * Note this uploads the file then links it to the session
	 * @param sessionId
	 * @param creatorId
	 * @param filename
	 * @param description
	 * @param content
	 */
	public void uploadRepositoryMultimedia(int sessionId, String creatorId, String filename, String description, Object content);
	public void createSessionMultimedia(int sessionId, Object multimedia);

	//delete
	public void removeRepositoryMultimedia(int multimediaId);
	public void removeSessionMultimedia(int sessionId, int multimediaId);
	
}
