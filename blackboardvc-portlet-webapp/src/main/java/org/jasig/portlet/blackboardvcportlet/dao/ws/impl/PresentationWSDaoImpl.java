/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ws.PresentationWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.WSDaoUtils;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardListRepositoryPresentation;
import com.elluminate.sas.BlackboardListSessionContent;
import com.elluminate.sas.BlackboardPresentationResponse;
import com.elluminate.sas.BlackboardPresentationResponseCollection;
import com.elluminate.sas.BlackboardRemoveRepositoryPresentation;
import com.elluminate.sas.BlackboardRemoveSessionPresentation;
import com.elluminate.sas.BlackboardSetSessionPresentation;
import com.elluminate.sas.BlackboardUploadRepositoryContent;
import com.elluminate.sas.ObjectFactory;

@Service
public class PresentationWSDaoImpl implements PresentationWSDao {
	
    protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private SASWebServiceOperations sasWebServiceOperations;
	
	@Autowired
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations)
	{
		this.sasWebServiceOperations = sasWebServiceOperations;
	}
	
	@Override
	public BlackboardPresentationResponse uploadPresentation(long sessionId, String creatorId, String filename, String description, DataHandler data) {
		BlackboardPresentationResponse response = uploadPresentation(creatorId, filename, description, data);
		if(!linkPresentationToSession(sessionId, response.getPresentationId()))
			logger.error("Error linking presentation ("+response.getPresentationId()+") to session ("+sessionId+"), however upload was successful.");
		return response;
	}
	
	@Override
	public BlackboardPresentationResponse uploadPresentation(String creatorId, String filename, String description, DataHandler data) {
		BlackboardUploadRepositoryContent request = new ObjectFactory().createBlackboardUploadRepositoryContent();
		request.setCreatorId(creatorId);
		request.setDescription(description);
		request.setFilename(filename);
		request.setContent(data);
		
		JAXBElement<BlackboardUploadRepositoryContent> createUploadRepositoryPresentation = new ObjectFactory().createUploadRepositoryPresentation(request);
		
		@SuppressWarnings("unchecked")
		JAXBElement<BlackboardPresentationResponseCollection> response = (JAXBElement<BlackboardPresentationResponseCollection>) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/UploadRepositoryPresentation", createUploadRepositoryPresentation);
		BlackboardPresentationResponseCollection unwrappedResponse = response.getValue();
		return DataAccessUtils.singleResult(unwrappedResponse.getPresentationResponses()); 
	}
	
	@Override
	public boolean linkPresentationToSession(long sessionId, long presentationId) {
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
	public List<BlackboardPresentationResponse> getSessionPresentations(long sessionId) {
		BlackboardListSessionContent request = new ObjectFactory().createBlackboardListSessionContent();
		request.setSessionId(sessionId);
		
		JAXBElement<BlackboardListSessionContent> createListSessionPresentation = new ObjectFactory().createListSessionPresentation(request);
		@SuppressWarnings("unchecked")
		final JAXBElement<BlackboardPresentationResponseCollection> objSessionResponse = (JAXBElement<BlackboardPresentationResponseCollection>)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionPresentation", createListSessionPresentation);
		
		return objSessionResponse.getValue().getPresentationResponses();
	}

	@Override
	public List<BlackboardPresentationResponse> getRepositoryPresentations(String creatorId, Long presentationId, String description) {
		BlackboardListRepositoryPresentation request = new ObjectFactory().createBlackboardListRepositoryPresentation();
		if(creatorId == null && presentationId == null && description == null) {
			throw new IllegalStateException("You must specify at least one piece of criteria");
		}
		if(creatorId != null) {
			request.setCreatorId(creatorId);
		}
		if(presentationId != null) {
			request.setPresentationId(presentationId);
		}
		
		if(description != null) {
			request.setDescription(description);
		}
		@SuppressWarnings("unchecked")
		final JAXBElement<BlackboardPresentationResponseCollection> response = (JAXBElement<BlackboardPresentationResponseCollection>)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRepositoryPresentation", request);
		final BlackboardPresentationResponseCollection objSessionResponse = (BlackboardPresentationResponseCollection)response.getValue();
		return objSessionResponse.getPresentationResponses();
	}

	@Override
	public boolean deletePresentation(long presentationId) {
		BlackboardRemoveRepositoryPresentation request = new ObjectFactory().createBlackboardRemoveRepositoryPresentation();
		request.setPresentationId(presentationId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveRepositoryPresentation", request));
		
	}

	@Override
	public boolean deleteSessionPresenation(long sessionId, long presentationId) {
		BlackboardRemoveSessionPresentation request = new ObjectFactory().createBlackboardRemoveSessionPresentation();
		request.setSessionId(sessionId);
		request.setPresentationId(presentationId);
		
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveSessionPresentation", request));
	}

}
