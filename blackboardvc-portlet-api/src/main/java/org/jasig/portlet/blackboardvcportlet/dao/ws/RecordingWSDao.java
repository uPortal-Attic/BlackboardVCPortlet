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

import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;

import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.elluminate.sas.BlackboardRecordingShortResponse;

public interface RecordingWSDao {
	/**
	 * Must specify at least one, can specify many
	 * @param userId
	 * @param groupingId
	 * @param sessionId
	 * @param creatorId
	 * @param startTime
	 * @param endTime
	 * @param sessionName
	 * @return
	 */
	public List<BlackboardRecordingLongResponse> getRecordingLong(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);

	/**
	 * Must specify at least one, can specify many
	 * @param userId
	 * @param groupingId
	 * @param sessionId
	 * @param creatorId
	 * @param startTime
	 * @param endTime
	 * @param sessionName
	 * @return
	 */
	public List<BlackboardRecordingShortResponse> getRecordingShort(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName);
	
	public boolean removeRecording(long recordingId);
	
	public String buildRecordingUrl(long recordingId);
	
	public boolean updateRecordingSecureSignOn(long recordingId, boolean secureSignOn);
	
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations);
}
