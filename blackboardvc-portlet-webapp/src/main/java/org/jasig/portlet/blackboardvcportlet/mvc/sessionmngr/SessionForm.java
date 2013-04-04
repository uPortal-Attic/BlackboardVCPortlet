/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author rgood
 */
public class SessionForm
{
	private MultipartFile presentationUpload;
	private MultipartFile multimediaUpload;
	private String action;
	private String sessionId;
	private String sessionName;
	private String startdate;
	private String starttime;
	private String enddate;
	private String endtime;
	private String allowInSessionInvites;
	private String creatorOrgUnit;
	private String boundaryTime;
	private String hideParticipantNames;
	private String maxCameras;
	private String maxTalkers;
	private String mustBeSupervised;
	private String permissionsOn;
	private String raiseHandOnEnter;
	private String recordingModeType;
	private String[] moderatorUids;
	private String[] moderatorEmails;
	private String[] moderatorDisplayNames;
	private String[] extParticipantEmails;
	private String[] extParticipantsDisplayNames;
	private String[] intParticipantUids;
	private String[] intParticipantEmails;
	private String[] intParticipantDisplayNames;

	public MultipartFile getPresentationUpload()
	{
		return presentationUpload;
	}

	public void setPresentationUpload(MultipartFile presentationUpload)
	{
		this.presentationUpload = presentationUpload;
	}

	public MultipartFile getMultimediaUpload()
	{
		return multimediaUpload;
	}

	public void setMultimediaUpload(MultipartFile multimediaUpload)
	{
		this.multimediaUpload = multimediaUpload;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getSessionName()
	{
		return sessionName;
	}

	public void setSessionName(String sessionName)
	{
		this.sessionName = sessionName;
	}

	public String getStartdate()
	{
		return startdate;
	}

	public void setStartdate(String startdate)
	{
		this.startdate = startdate;
	}

	public String getStarttime()
	{
		return starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}

	public String getEnddate()
	{
		return enddate;
	}

	public void setEnddate(String enddate)
	{
		this.enddate = enddate;
	}

	public String getEndtime()
	{
		return endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}

	public String getAllowInSessionInvites()
	{
		return allowInSessionInvites;
	}

	public void setAllowInSessionInvites(String allowInSessionInvites)
	{
		this.allowInSessionInvites = allowInSessionInvites;
	}

	public String getCreatorOrgUnit()
	{
		return creatorOrgUnit;
	}

	public void setCreatorOrgUnit(String creatorOrgUnit)
	{
		this.creatorOrgUnit = creatorOrgUnit;
	}

	public String getBoundaryTime()
	{
		return boundaryTime;
	}

	public void setBoundaryTime(String boundaryTime)
	{
		this.boundaryTime = boundaryTime;
	}

	public String getHideParticipantNames()
	{
		return hideParticipantNames;
	}

	public void setHideParticipantNames(String hideParticipantNames)
	{
		this.hideParticipantNames = hideParticipantNames;
	}

	public String getMaxCameras()
	{
		return maxCameras;
	}

	public void setMaxCameras(String maxCameras)
	{
		this.maxCameras = maxCameras;
	}

	public String getMaxTalkers()
	{
		return maxTalkers;
	}

	public void setMaxTalkers(String maxTalkers)
	{
		this.maxTalkers = maxTalkers;
	}

	public String getMustBeSupervised()
	{
		return mustBeSupervised;
	}

	public void setMustBeSupervised(String mustBeSupervised)
	{
		this.mustBeSupervised = mustBeSupervised;
	}

	public String getPermissionsOn()
	{
		return permissionsOn;
	}

	public void setPermissionsOn(String permissionsOn)
	{
		this.permissionsOn = permissionsOn;
	}

	public String getRaiseHandOnEnter()
	{
		return raiseHandOnEnter;
	}

	public void setRaiseHandOnEnter(String raiseHandOnEnter)
	{
		this.raiseHandOnEnter = raiseHandOnEnter;
	}

	public String getRecordingModeType()
	{
		return recordingModeType;
	}

	public void setRecordingModeType(String recordingModeType)
	{
		this.recordingModeType = recordingModeType;
	}

	public String[] getModeratorUids()
	{
		return moderatorUids;
	}

	public void setModeratorUids(String[] moderatorUids)
	{
		this.moderatorUids = moderatorUids;
	}

	public String[] getModeratorEmails()
	{
		return moderatorEmails;
	}

	public void setModeratorEmails(String[] moderatorEmails)
	{
		this.moderatorEmails = moderatorEmails;
	}

	public String[] getModeratorDisplayNames()
	{
		return moderatorDisplayNames;
	}

	public void setModeratorDisplayNames(String[] moderatorDisplayNames)
	{
		this.moderatorDisplayNames = moderatorDisplayNames;
	}

	public String[] getExtParticipantEmails()
	{
		return extParticipantEmails;
	}

	public void setExtParticipantEmails(String[] extParticipantEmails)
	{
		this.extParticipantEmails = extParticipantEmails;
	}

	public String[] getExtParticipantsDisplayNames()
	{
		return extParticipantsDisplayNames;
	}

	public void setExtParticipantsDisplayNames(String[] extParticipantsDisplayNames)
	{
		this.extParticipantsDisplayNames = extParticipantsDisplayNames;
	}

	public String[] getIntParticipantUids()
	{
		return intParticipantUids;
	}

	public void setIntParticipantUids(String[] intParticipantUids)
	{
		this.intParticipantUids = intParticipantUids;
	}

	public String[] getIntParticipantEmails()
	{
		return intParticipantEmails;
	}

	public void setIntParticipantEmails(String[] intParticipantEmails)
	{
		this.intParticipantEmails = intParticipantEmails;
	}

	public String[] getIntParticipantDisplayNames()
	{
		return intParticipantDisplayNames;
	}

	public void setIntParticipantDisplayNames(String[] intParticipantDisplayNames)
	{
		this.intParticipantDisplayNames = intParticipantDisplayNames;
	}
}