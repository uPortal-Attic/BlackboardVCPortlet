package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import javax.activation.DataHandler;

import com.elluminate.sas.BlackboardMultimediaResponse;

public interface MultimediaWSDao {
	//read
	public List<BlackboardMultimediaResponse> getRepositoryMultimedias(String creatorId, Long multimediaId, String description);
	public List<BlackboardMultimediaResponse> getSessionRepositoryMultimedias(Long sessionId);
	
	//create
	public BlackboardMultimediaResponse uploadRepositoryMultimedia(String creatorId, String filename, String description, DataHandler content);
	public BlackboardMultimediaResponse createSessionMultimedia(Long sessionId, String creatorId, String filename, String description, DataHandler content);
	public boolean linkSessionToMultimedia(Long sessionId, Long multimediaId);

	//delete
	public boolean removeRepositoryMultimedia(Long multimediaId);
	public boolean removeSessionMultimedia(Long sessionId, Long multimediaId);
	
}
