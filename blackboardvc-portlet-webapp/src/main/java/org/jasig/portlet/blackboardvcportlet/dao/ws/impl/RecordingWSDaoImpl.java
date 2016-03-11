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

import org.jasig.portlet.blackboardvcportlet.dao.ws.RecordingWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.WSDaoUtils;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardBuildRecordingUrl;
import com.elluminate.sas.BlackboardListRecordingLong;
import com.elluminate.sas.BlackboardListRecordingLongResponseCollection;
import com.elluminate.sas.BlackboardListRecordingShort;
import com.elluminate.sas.BlackboardListRecordingShortResponseCollection;
import com.elluminate.sas.BlackboardRecordingLongResponse;
import com.elluminate.sas.BlackboardRecordingShortResponse;
import com.elluminate.sas.BlackboardRemoveRecording;
import com.elluminate.sas.BlackboardSetRecordingSecureSignOn;
import com.elluminate.sas.BlackboardUrlResponse;
import com.elluminate.sas.ObjectFactory;

import static org.jasig.portlet.blackboardvcportlet.dao.ws.WSDaoUtils.isSuccessful;

@Service
public class RecordingWSDaoImpl implements RecordingWSDao {
	
	private SASWebServiceOperations sasWebServiceOperations;
	
	@Autowired
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations)
	{
		this.sasWebServiceOperations = sasWebServiceOperations;
	}

	@Override
	public List<BlackboardRecordingLongResponse> getRecordingLong(String userId, String groupingId, Long sessionId, String creatorId,
			           												Long startTime, Long endTime, String sessionName) {
		BlackboardListRecordingLong request = new ObjectFactory().createBlackboardListRecordingLong();
		
		if(userId == null && groupingId == null && sessionId == null && creatorId == null && startTime == null && endTime == null && sessionName == null) {
			throw new IllegalStateException("You must specify at least one piece of criteria");
		}
		
		if(userId != null) {
			request.setUserId(userId);
		}
		if(groupingId != null) {
			request.setGroupingId(groupingId);
		}
		if(sessionId != null) {
			request.setSessionId(sessionId);
		}
		if(creatorId != null) {
			request.setCreatorId(creatorId);
		}
		if(startTime != null) {
			request.setStartTime(startTime);
		}
		if(endTime != null) {
			request.setEndTime(endTime);
		}
		if (sessionName != null) {
			request.setSessionName(sessionName);
		}
		
		BlackboardListRecordingLongResponseCollection response = (BlackboardListRecordingLongResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRecordingLong", request);
		return response.getRecordingLongResponses();
	}

	@Override
	public List<BlackboardRecordingShortResponse> getRecordingShort(String userId, String groupingId, Long sessionId, String creatorId, Long startTime, Long endTime, String sessionName) {
		BlackboardListRecordingShort request = new ObjectFactory().createBlackboardListRecordingShort();
		
		if(userId == null && groupingId == null && sessionId == null && creatorId == null && startTime == null && endTime == null && sessionName == null) {
			throw new IllegalStateException("You must specify at least one piece of criteria");
		}
		
		if(userId != null) {
			request.setUserId(userId);
		}
		if(groupingId != null) {
			request.setGroupingId(groupingId);
		}
		if(sessionId != null) {
			request.setSessionId(sessionId);
		}
		if(creatorId != null) {
			request.setCreatorId(creatorId);
		}
		if(startTime != null) {
			request.setStartTime(startTime);
		}
		if(endTime != null) {
			request.setEndTime(endTime);
		}
		if (sessionName != null) {
			request.setSessionName(sessionName);
		}
		
		BlackboardListRecordingShortResponseCollection response = (BlackboardListRecordingShortResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRecordingShort", request);
		return response.getRecordingShortResponses();
	}

	@Override
	public boolean removeRecording(long recordingId) {
		BlackboardRemoveRecording request = new ObjectFactory().createBlackboardRemoveRecording();
		request.setRecordingId(recordingId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveRecording", request));
		
	}

	@Override
	public String buildRecordingUrl(long recordingId) {
		BlackboardBuildRecordingUrl request = new ObjectFactory().createBlackboardBuildRecordingUrl();
		request.setRecordingId(recordingId);
		BlackboardUrlResponse response = (BlackboardUrlResponse)sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/BuildRecordingUrl", request);
		
		return response.getUrl();
	}

	@Override
	public boolean updateRecordingSecureSignOn(long recordingId, boolean secureSignOn) {
		BlackboardSetRecordingSecureSignOn request = new ObjectFactory().createBlackboardSetRecordingSecureSignOn();
		request.setRecordingId(recordingId);
		request.setSecureSignOn(secureSignOn);
		return isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetRecordingSecureSignOn", request));
	}

}
