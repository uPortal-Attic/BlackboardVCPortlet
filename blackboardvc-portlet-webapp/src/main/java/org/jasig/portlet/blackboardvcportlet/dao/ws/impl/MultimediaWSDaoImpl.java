package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.activation.DataHandler;

import org.jasig.portlet.blackboardvcportlet.dao.ws.MultimediaWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.WSDaoUtils;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;

import com.elluminate.sas.BlackboardListRepositoryMultimedia;
import com.elluminate.sas.BlackboardListSessionContent;
import com.elluminate.sas.BlackboardMultimediaResponse;
import com.elluminate.sas.BlackboardMultimediaResponseCollection;
import com.elluminate.sas.BlackboardRemoveRepositoryMultimedia;
import com.elluminate.sas.BlackboardRemoveSessionMultimedia;
import com.elluminate.sas.BlackboardSetSessionMultimedia;
import com.elluminate.sas.BlackboardUploadRepositoryContent;
import com.elluminate.sas.ObjectFactory;

public class MultimediaWSDaoImpl implements MultimediaWSDao { 
	
	private static final Logger logger = LoggerFactory.getLogger(MultimediaWSDaoImpl.class);
	
	private SASWebServiceOperations sasWebServiceOperations;
	
	@Autowired
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations)
	{
		this.sasWebServiceOperations = sasWebServiceOperations;
	}

	@Override
	public List<BlackboardMultimediaResponse> getRepositoryMultimedias(String creatorId, Long multimediaId, String description) {
		BlackboardListRepositoryMultimedia request = new ObjectFactory().createBlackboardListRepositoryMultimedia();
		if(creatorId != null) {
			request.setCreatorId(creatorId);
		}
		if(multimediaId != null) {
			request.setMultimediaId(multimediaId);
		}
		
		if(description != null) {
			request.setDescription(description);
		}
		final BlackboardMultimediaResponseCollection objSessionResponse = (BlackboardMultimediaResponseCollection)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRespositoryMultimedia", request);
		return objSessionResponse.getMultimediaResponses();
	}

	@Override
	public List<BlackboardMultimediaResponse> getSessionRepositoryMultimedias(Long sessionId) {
		BlackboardListSessionContent request = new ObjectFactory().createBlackboardListSessionContent();
		if(sessionId != null) {
			request.setSessionId(sessionId);
		}
		final BlackboardMultimediaResponseCollection objSessionResponse = (BlackboardMultimediaResponseCollection)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionMultimedia", request);
		return objSessionResponse.getMultimediaResponses();
	}

	@Override
	public BlackboardMultimediaResponse uploadRepositoryMultimedia(String creatorId,
			String filename, String description, DataHandler content) {
		
		BlackboardUploadRepositoryContent request = new ObjectFactory().createBlackboardUploadRepositoryContent();
		request.setCreatorId(creatorId);
		request.setDescription(description);
		request.setFilename(filename);
		request.setContent(content);
		
		BlackboardMultimediaResponseCollection response = (BlackboardMultimediaResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/UploadRepositoryMultimedia", request);
		return DataAccessUtils.singleResult(response.getMultimediaResponses());
		
	}

	@Override
	public BlackboardMultimediaResponse createSessionMultimedia(Long sessionId, String creatorId, String filename, String description, DataHandler content) {
		BlackboardMultimediaResponse multimediaMetaData = uploadRepositoryMultimedia(creatorId, filename, description, content);
		if(!linkSessionToMultimedia(sessionId, multimediaMetaData.getMultimediaId())) {
			logger.error("Error linking multimedia ("+multimediaMetaData.getMultimediaId()+") to session ("+sessionId+"), however upload was successful.");
		}
		return multimediaMetaData;
	}
	
	@Override
	public boolean linkSessionToMultimedia(Long sessionId, Long multimediaId) {
		BlackboardSetSessionMultimedia request = new ObjectFactory().createBlackboardSetSessionMultimedia();
		request.setMultimediaIds(Long.toString(multimediaId));
		request.setSessionId(sessionId);
		
		if(WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetSessionMultimedia", request))) {
			return true;
		} else {
			logger.error("Issue linking the multimedia id " + multimediaId + " with session " + sessionId);
			return false;
		}
	}

	@Override
	public boolean removeRepositoryMultimedia(int multimediaId) {
		BlackboardRemoveRepositoryMultimedia request = new ObjectFactory().createBlackboardRemoveRepositoryMultimedia();
		request.setMultimediaId(multimediaId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveRepositoryMultimedia", request));
		
	}

	@Override
	public boolean removeSessionMultimedia(int sessionId, int multimediaId) {
		
		//TODO : Riddle me this, if I delete this link and the multimedia has no more session ties, it is also deleted? We may want to have a job that checks for this on occation
		
		BlackboardRemoveSessionMultimedia request = new ObjectFactory().createBlackboardRemoveSessionMultimedia();
		request.setSessionId(sessionId);
		request.setMultimediaId(multimediaId);
		
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveSessionMultimedia", request));
	}
	

}
