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
package org.jasig.portlet.blackboardvcportlet.dao.ws;

import java.util.List;

import javax.activation.DataHandler;

import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;

import com.elluminate.sas.BlackboardPresentationResponse;

public interface PresentationWSDao {
	
	//create
	/**
	 * Note this first uploads the presentation then links it to the session
	 * @param sessionId
	 * @param creatorId
	 * @param filename
	 * @param description
	 * @param content
	 * @return 
	 */
	public BlackboardPresentationResponse uploadPresentation(long sessionId, String creatorId, String filename, String description, DataHandler data);
	public BlackboardPresentationResponse uploadPresentation(String creatorId, String filename, String description, DataHandler data);
	public boolean linkPresentationToSession(long sessionId, long presentationId);

	//read
	public List<BlackboardPresentationResponse> getSessionPresentations(long sessionId);
	/**
	 * Must specify at least one piece of criteria, and can have multiple
	 * @param creatorId
	 * @param presentationId Long instead of long so we can pass in null to the search
	 * @param description
	 * @return
	 */
	public List<BlackboardPresentationResponse> getRepositoryPresentations(String creatorId, Long presentationId, String description);
	
	//delete
	public boolean deletePresentation(long presentationId);
	public boolean deleteSessionPresenation(long sessionId, long presenationId);
	
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations);
	
}
