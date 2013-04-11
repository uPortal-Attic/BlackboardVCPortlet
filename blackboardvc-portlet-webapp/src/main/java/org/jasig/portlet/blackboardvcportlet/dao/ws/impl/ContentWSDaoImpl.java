package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import javax.activation.DataHandler;

import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardListSessionContent;
import com.elluminate.sas.BlackboardUploadRepositoryContent;
import com.elluminate.sas.ObjectFactory;

@Service
public abstract class ContentWSDaoImpl {
	
	public enum ContentType {Multimedia, Presentation} ;
	
	SASWebServiceOperations sasWebServiceOperations;
	
	@Autowired
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations)
	{
		this.sasWebServiceOperations = sasWebServiceOperations;
	}
	
	Object uploadContent(String creatorId, String filename, String description, DataHandler data, ContentType type) {
		BlackboardUploadRepositoryContent request = new ObjectFactory().createBlackboardUploadRepositoryContent();
		request.setCreatorId(creatorId);
		request.setDescription(description);
		request.setFilename(filename);
		request.setContent(data);
		
		return sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/UploadRepository" + type, request);
	}
	
	Object getSessionContent(Long sessionId, ContentType type) {
		BlackboardListSessionContent request = new ObjectFactory().createBlackboardListSessionContent();
		if(sessionId != null) {
			request.setSessionId(sessionId);
		}
		final Object objSessionResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSession" + type, request);
		return objSessionResponse;
	}
}
