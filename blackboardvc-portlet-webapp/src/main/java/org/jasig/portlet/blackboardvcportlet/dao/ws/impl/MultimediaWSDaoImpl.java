package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ws.MultimediaWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.WSDaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardListRepositoryMultimedia;
import com.elluminate.sas.BlackboardListSessionContent;
import com.elluminate.sas.BlackboardMultimediaResponse;
import com.elluminate.sas.BlackboardMultimediaResponseCollection;
import com.elluminate.sas.BlackboardRemoveRepositoryMultimedia;
import com.elluminate.sas.BlackboardRemoveSessionMultimedia;
import com.elluminate.sas.BlackboardSetSessionMultimedia;
import com.elluminate.sas.BlackboardUploadRepositoryContent;
import com.elluminate.sas.ObjectFactory;

@Service
public class MultimediaWSDaoImpl extends ContentWSDaoImpl implements MultimediaWSDao { 
	
	private static final Logger logger = LoggerFactory.getLogger(MultimediaWSDaoImpl.class);
	
	@Override
	public List<BlackboardMultimediaResponse> getRepositoryMultimedias(String creatorId, Long multimediaId, String description) {
		BlackboardListRepositoryMultimedia request = new ObjectFactory().createBlackboardListRepositoryMultimedia();
		if(creatorId == null && multimediaId == null && description == null) {
			throw new IllegalStateException("You must specify a creator, multimedia ID, or a description");
		}
		
		if(creatorId != null) {
			request.setCreatorId(creatorId);
		}
		if(multimediaId != null) {
			request.setMultimediaId(multimediaId);
		}
		
		if(description != null) {
			request.setDescription(description);
		}
		@SuppressWarnings("unchecked")
		final JAXBElement<BlackboardMultimediaResponseCollection> objSessionResponse = (JAXBElement<BlackboardMultimediaResponseCollection>)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRespositoryMultimedia", request);
		return objSessionResponse.getValue().getMultimediaResponses();
	}

	@Override
	public List<BlackboardMultimediaResponse> getSessionRepositoryMultimedias(long sessionId) {
		BlackboardListSessionContent request = new ObjectFactory().createBlackboardListSessionContent();
		request.setSessionId(sessionId);
		
		JAXBElement<BlackboardListSessionContent> createListSessionMultimedia = new ObjectFactory().createListSessionMultimedia(request);
		@SuppressWarnings("unchecked")
		final JAXBElement<BlackboardMultimediaResponseCollection> objSessionResponse = (JAXBElement<BlackboardMultimediaResponseCollection>)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionMultimedia", createListSessionMultimedia);
		
		return objSessionResponse.getValue().getMultimediaResponses();
	}

	@Override
	public BlackboardMultimediaResponse uploadRepositoryMultimedia(String creatorId,
			String filename, String description, DataHandler content) {
		BlackboardUploadRepositoryContent request = new ObjectFactory().createBlackboardUploadRepositoryContent();
		request.setCreatorId(creatorId);
		request.setDescription(description);
		request.setFilename(filename);
		request.setContent(content);
		
		JAXBElement<BlackboardUploadRepositoryContent> realRequest = new ObjectFactory().createUploadRepositoryMultimedia(request);
		
		@SuppressWarnings("unchecked")
		JAXBElement<BlackboardMultimediaResponseCollection> response = (JAXBElement<BlackboardMultimediaResponseCollection>) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/UploadRepositoryMultimedia", realRequest); 
		return DataAccessUtils.singleResult(response.getValue().getMultimediaResponses());
		
	}

	@Override
	public BlackboardMultimediaResponse createSessionMultimedia(long sessionId, String creatorId, String filename, String description, DataHandler content) {
		BlackboardMultimediaResponse multimediaMetaData = uploadRepositoryMultimedia(creatorId, filename, description, content);
		if(!linkSessionToMultimedia(sessionId, multimediaMetaData.getMultimediaId())) {
			logger.error("Error linking multimedia ("+multimediaMetaData.getMultimediaId()+") to session ("+sessionId+"), however upload was successful.");
		}
		return multimediaMetaData;
	}
	
	@Override
	public boolean linkSessionToMultimedia(long sessionId, long multimediaId) {
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
	public boolean removeRepositoryMultimedia(long multimediaId) {
		BlackboardRemoveRepositoryMultimedia request = new ObjectFactory().createBlackboardRemoveRepositoryMultimedia();
		request.setMultimediaId(multimediaId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveRepositoryMultimedia", request));
		
	}

	@Override
	public boolean removeSessionMultimedia(long sessionId, long multimediaId) {
		BlackboardRemoveSessionMultimedia request = new ObjectFactory().createBlackboardRemoveSessionMultimedia();
		request.setSessionId(sessionId);
		request.setMultimediaId(multimediaId);
		
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveSessionMultimedia", request));
	}
	

}
