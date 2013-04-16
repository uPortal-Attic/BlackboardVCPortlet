package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import javax.activation.DataHandler;

import com.elluminate.sas.BlackboardMultimediaResponse;

public interface MultimediaWSDao {
	//read
	/**
	 * Note that you just need to specify one of these, but can do multiple pieces of criteria
	 * @param creatorId
	 * @param multimediaId a Long instead of long so we can pass in null
	 * @param description
	 * @return
	 */
	public List<BlackboardMultimediaResponse> getRepositoryMultimedias(String creatorId, Long multimediaId, String description);
	public List<BlackboardMultimediaResponse> getSessionRepositoryMultimedias(long sessionId);
	
	//create
	public BlackboardMultimediaResponse uploadRepositoryMultimedia(String creatorId, String filename, String description, DataHandler content);
	public BlackboardMultimediaResponse createSessionMultimedia(long sessionId, String creatorId, String filename, String description, DataHandler content);
	public boolean linkSessionToMultimedia(long sessionId, long multimediaId);

	//delete
	public boolean removeRepositoryMultimedia(long multimediaId);
	public boolean removeSessionMultimedia(long sessionId, long multimediaId);
	
}
