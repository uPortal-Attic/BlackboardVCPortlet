package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import javax.activation.DataHandler;

import com.elluminate.sas.BlackboardMultimediaResponse;

public interface MultimediaWSDao {
	//read
	/**
	 * Note that you just need to specify one of these, but can do multiple pieces of criteria
	 * @param creatorId
	 * @param bbMultimediaId a Long instead of long so we can pass in null
	 * @param description
	 * @return
	 */
    List<BlackboardMultimediaResponse> getRepositoryMultimedias(String creatorId, Long bbMultimediaId, String description);
    List<BlackboardMultimediaResponse> getSessionMultimedias(long bbSessionId);
	
	//create
    BlackboardMultimediaResponse uploadRepositoryMultimedia(String creatorId, String filename, String description, DataHandler content);
    BlackboardMultimediaResponse createSessionMultimedia(long bbSessionId, String creatorId, String filename, String description, DataHandler content);
    boolean linkSessionToMultimedia(long bbSessionId, long bbMultimediaId);

	//delete
    boolean removeRepositoryMultimedia(long bbMultimediaId);
    boolean removeSessionMultimedia(long bbSessionId, long bbMultimediaId);
	
}
