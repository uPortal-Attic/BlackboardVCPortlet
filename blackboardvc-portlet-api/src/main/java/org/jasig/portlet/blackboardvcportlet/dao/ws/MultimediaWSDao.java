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
	
    //for unit tests
    public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations);
}
