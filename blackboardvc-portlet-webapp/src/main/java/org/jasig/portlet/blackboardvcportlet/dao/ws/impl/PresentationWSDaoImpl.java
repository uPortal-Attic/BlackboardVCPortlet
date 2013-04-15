package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ws.PresentationWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.WSDaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardListRepositoryPresentation;
import com.elluminate.sas.BlackboardListSessionContent;
import com.elluminate.sas.BlackboardMultimediaResponseCollection;
import com.elluminate.sas.BlackboardPresentationResponse;
import com.elluminate.sas.BlackboardPresentationResponseCollection;
import com.elluminate.sas.BlackboardRemoveRepositoryPresentation;
import com.elluminate.sas.BlackboardRemoveSessionPresentation;
import com.elluminate.sas.BlackboardSetSessionPresentation;
import com.elluminate.sas.ObjectFactory;

@Service
public class PresentationWSDaoImpl extends ContentWSDaoImpl implements PresentationWSDao {
	
	private static final Logger logger = LoggerFactory.getLogger(PresentationWSDaoImpl.class);
	
	@Override
	public BlackboardPresentationResponse uploadPresentation(Long sessionId, String creatorId, String filename, String description, DataHandler data) {
		BlackboardPresentationResponse response = uploadPresentation(creatorId, filename, description, data);
		if(!linkPresentationToSession(sessionId, response.getPresentationId()))
			logger.error("Error linking presentation ("+response.getPresentationId()+") to session ("+sessionId+"), however upload was successful.");
		return response;
	}
	
	@Override
	public BlackboardPresentationResponse uploadPresentation(String creatorId, String filename, String description, DataHandler data) {
		BlackboardPresentationResponseCollection response = (BlackboardPresentationResponseCollection) uploadContent(creatorId, filename, description, data, ContentType.Presentation);
		return DataAccessUtils.singleResult(response.getPresentationResponses()); 
	}
	
	@Override
	public boolean linkPresentationToSession(Long sessionId, Long presentationId) {
		BlackboardSetSessionPresentation request = new ObjectFactory().createBlackboardSetSessionPresentation();
		request.setPresentationId(presentationId);
		request.setSessionId(sessionId);
		
		if(WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetSessionPresentation", request))) {
			return true;
		} else {
			logger.error("Issue linking the presentation id " + presentationId + " with session " + sessionId);
			return false;
		}
	}

	@Override
	public List<BlackboardPresentationResponse> getSessionPresentations(Long sessionId) {
		BlackboardListSessionContent request = new ObjectFactory().createBlackboardListSessionContent();
		if(sessionId != null) {
			request.setSessionId(sessionId);
		}
		JAXBElement<BlackboardListSessionContent> createListSessionPresentation = new ObjectFactory().createListSessionPresentation(request);
		final JAXBElement<BlackboardPresentationResponseCollection> objSessionResponse = (JAXBElement<BlackboardPresentationResponseCollection>)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionPresentation", createListSessionPresentation);
		
		return objSessionResponse.getValue().getPresentationResponses();
	}

	@Override
	public List<BlackboardPresentationResponse> getRepositoryPresentations(String creatorId, Long presentationId, String description) {
		BlackboardListRepositoryPresentation request = new ObjectFactory().createBlackboardListRepositoryPresentation();
		if(creatorId != null) {
			request.setCreatorId(creatorId);
		}
		if(presentationId != null) {
			request.setPresentationId(presentationId);
		}
		
		if(description != null) {
			request.setDescription(description);
		}
		final BlackboardPresentationResponseCollection objSessionResponse = (BlackboardPresentationResponseCollection)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRepositoryPresentation", request);
		return objSessionResponse.getPresentationResponses();
	}

	@Override
	public boolean deletePresentation(Long presentationId) {
		BlackboardRemoveRepositoryPresentation request = new ObjectFactory().createBlackboardRemoveRepositoryPresentation();
		request.setPresentationId(presentationId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveRepositoryPresentation", request));
		
	}

	@Override
	public boolean deleteSessionPresenation(Long sessionId, Long presentationId) {
		//TODO : Riddle me this, if I delete this link and the presentation has no more session ties, it is also deleted? We may want to have a job that checks for this on occasion
		
		BlackboardRemoveSessionPresentation request = new ObjectFactory().createBlackboardRemoveSessionPresentation();
		request.setSessionId(sessionId);
		request.setPresentationId(presentationId);
		
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveSessionPresentation", request));
	}

}
