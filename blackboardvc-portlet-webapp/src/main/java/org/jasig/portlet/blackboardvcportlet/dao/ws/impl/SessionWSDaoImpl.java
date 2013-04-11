package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.portlet.UnavailableException;
import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.RecordingMode;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;

import com.elluminate.sas.BlackboardBuildSessionUrl;
import com.elluminate.sas.BlackboardClearSessionUserList;
import com.elluminate.sas.BlackboardGetServerConfigurationResponseCollection;
import com.elluminate.sas.BlackboardListSession;
import com.elluminate.sas.BlackboardListSessionAttendance;
import com.elluminate.sas.BlackboardListSessionAttendanceResponseCollection;
import com.elluminate.sas.BlackboardRemoveSession;
import com.elluminate.sas.BlackboardSessionAttendanceResponse;
import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSessionResponseCollection;
import com.elluminate.sas.BlackboardSessionTelephony;
import com.elluminate.sas.BlackboardSessionTelephonyResponseCollection;
import com.elluminate.sas.BlackboardSetSession;
import com.elluminate.sas.BlackboardSetSessionTelephony;
import com.elluminate.sas.BlackboardSuccessResponse;
import com.elluminate.sas.BlackboardUpdateSession;
import com.elluminate.sas.BlackboardUrlResponse;
import com.elluminate.sas.ObjectFactory;

public class SessionWSDaoImpl implements SessionWSDao {
	
	private SASWebServiceOperations sasWebServiceOperations;
	
	@Autowired
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations)
	{
		this.sasWebServiceOperations = sasWebServiceOperations;
	}

	@Override
	public BlackboardSessionResponse createSession(ConferenceUser user, SessionForm sessionForm, boolean fullAccess) {
		final BlackboardSetSession setSession = new BlackboardSetSession();
        setSession.setCreatorId(user.getEmail());
        setSession.setSessionName(sessionForm.getSessionName());
        setSession.setStartTime(sessionForm.getStartTime().getMillis());
        setSession.setEndTime(sessionForm.getEndTime().getMillis());
        setSession.setBoundaryTime(sessionForm.getBoundaryTime());

        if (fullAccess) {
            setSession.setMaxTalkers(sessionForm.getMaxTalkers());
            setSession.setMaxCameras(sessionForm.getMaxCameras());
            setSession.setMustBeSupervised(sessionForm.isMustBeSupervised());
            setSession.setPermissionsOn(sessionForm.isPermissionsOn());
            setSession.setRaiseHandOnEnter(sessionForm.isRaiseHandOnEnter());
            final RecordingMode recordingMode = sessionForm.getRecordingMode();
            if (recordingMode != null) {
                setSession.setRecordingModeType(recordingMode.getBlackboardRecordingMode());
            }
            setSession.setHideParticipantNames(sessionForm.isHideParticipantNames());
            setSession.setAllowInSessionInvites(sessionForm.isAllowInSessionInvites());
        }

        final Object objSessionResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetSession", setSession);
        JAXBElement<BlackboardSessionResponseCollection> jaxbSessionResponse = (JAXBElement<BlackboardSessionResponseCollection>) objSessionResponse;
        
        final BlackboardSessionResponseCollection sessionResponses = jaxbSessionResponse.getValue();
        final BlackboardSessionResponse sessionResponse = DataAccessUtils.singleResult(sessionResponses.getSessionResponses());
        return sessionResponse;
		
	}
	
	@Override
	public String buildSessionUrl(long sessionId, String displayName) {
		BlackboardBuildSessionUrl buildGuestUrlRequest = new BlackboardBuildSessionUrl();
        buildGuestUrlRequest.setSessionId(sessionId);
        buildGuestUrlRequest.setDisplayName(displayName);
        final Object objGuestUrlResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/BuildSessionUrl", buildGuestUrlRequest);
        JAXBElement<BlackboardUrlResponse> jaxbGuestUrlResponse = (JAXBElement<BlackboardUrlResponse>) objGuestUrlResponse;
        return  jaxbGuestUrlResponse.getValue().getUrl();
		
	}

	@Override
	public boolean createSessionTelephony(int sessionId, BlackboardSetSessionTelephony telephony) {
		telephony.setSessionId(sessionId);
		final Object objSessionTelephonyResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetSessionTelephony", telephony);
		JAXBElement<BlackboardSuccessResponse> response = (JAXBElement<BlackboardSuccessResponse>) objSessionTelephonyResponse;
		return response.getValue().isSuccess();
	}

	@Override
	public List<BlackboardSessionResponse> getSessions(String userId, String groupingId, Long sessionId,
			String creatorId, Long startTime, Long endTime, String sessionName) {
		//build search request
		BlackboardListSession request = new ObjectFactory().createBlackboardListSession();
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
		
		if(sessionName != null) {
			request.setSessionName(sessionName);
		}
		
		BlackboardSessionResponseCollection responseCollection = (BlackboardSessionResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSession", request);
		return responseCollection.getSessionResponses();
	}

	@Override
	public List<BlackboardSessionAttendanceResponse> getSessionAttendance(int sessionId, Object startTime) {
		BlackboardListSessionAttendance request = new ObjectFactory().createBlackboardListSessionAttendance();
		BlackboardListSessionAttendanceResponseCollection responseCollection = (BlackboardListSessionAttendanceResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionAttendance", request);
		return responseCollection.getSessionAttendanceResponses();
	}

	@Override
	public List getSessionTelephony(int sessionId) {
		BlackboardSessionTelephony request = new ObjectFactory().createBlackboardSessionTelephony();
		request.setSessionId(sessionId);
		final BlackboardSessionTelephonyResponseCollection response = (BlackboardSessionTelephonyResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionTelephony", request);
		return response.getSessionTelephonyResponses();
	}

	@Override
	public BlackboardSessionResponse updateSession(ConferenceUser user, SessionForm sessionForm, boolean fullAccess) {
		final BlackboardUpdateSession updateSession = new ObjectFactory().createBlackboardUpdateSession();
		updateSession.setSessionName(sessionForm.getSessionName());
		updateSession.setStartTime(sessionForm.getStartTime().getMillis());
		updateSession.setEndTime(sessionForm.getEndTime().getMillis());
		updateSession.setBoundaryTime(sessionForm.getBoundaryTime());

        if (fullAccess) {
        	updateSession.setMaxTalkers(sessionForm.getMaxTalkers());
        	updateSession.setMaxCameras(sessionForm.getMaxCameras());
        	updateSession.setMustBeSupervised(sessionForm.isMustBeSupervised());
        	updateSession.setPermissionsOn(sessionForm.isPermissionsOn());
        	updateSession.setRaiseHandOnEnter(sessionForm.isRaiseHandOnEnter());
            final RecordingMode recordingMode = sessionForm.getRecordingMode();
            if (recordingMode != null) {
            	updateSession.setRecordingModeType(recordingMode.getBlackboardRecordingMode());
            }
            updateSession.setHideParticipantNames(sessionForm.isHideParticipantNames());
            updateSession.setAllowInSessionInvites(sessionForm.isAllowInSessionInvites());
        }
        
        final Object objSessionResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/UpdateSession", updateSession);
        BlackboardSessionResponseCollection response = (BlackboardSessionResponseCollection) objSessionResponse;
        return DataAccessUtils.singleResult(response.getSessionResponses());
	}

	@Override
	public boolean deleteSession(int sessionId) {
		BlackboardRemoveSession request = new ObjectFactory().createBlackboardRemoveSession();
		request.setSessionId(sessionId);
		
		final Object response = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveSession", request);
		return (response instanceof BlackboardSuccessResponse && ((BlackboardSuccessResponse)response).isSuccess());
	}

	@Override
	public boolean clearSessionChairList(int sessionId) {
		return clearSessionUserList(sessionId,true);
	}

	@Override
	public boolean clearSessionNonChairList(int sessionId) {
		return clearSessionUserList(sessionId,false);
		
	}
	
	private boolean clearSessionUserList(int sessionId, boolean isChairList) {
		BlackboardClearSessionUserList request = new ObjectFactory().createBlackboardClearSessionUserList();
		request.setSessionId(sessionId);
		final Object response = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ClearSession"+ (isChairList ? "" : "Non"+"ChairList") , request);
		return (response instanceof BlackboardSuccessResponse && ((BlackboardSuccessResponse)response).isSuccess());
	}
}
