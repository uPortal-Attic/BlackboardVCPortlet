package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBElement.GlobalScope;
import javax.xml.namespace.QName;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.dao.ws.WSDaoUtils;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.RecordingMode;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BlackboardBuildSessionUrl;
import com.elluminate.sas.BlackboardClearSessionUserList;
import com.elluminate.sas.BlackboardListSession;
import com.elluminate.sas.BlackboardListSessionAttendance;
import com.elluminate.sas.BlackboardListSessionAttendanceResponseCollection;
import com.elluminate.sas.BlackboardRemoveSession;
import com.elluminate.sas.BlackboardSessionAttendanceResponse;
import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSessionResponseCollection;
import com.elluminate.sas.BlackboardSessionTelephony;
import com.elluminate.sas.BlackboardSessionTelephonyResponse;
import com.elluminate.sas.BlackboardSessionTelephonyResponseCollection;
import com.elluminate.sas.BlackboardSetSession;
import com.elluminate.sas.BlackboardSetSessionTelephony;
import com.elluminate.sas.BlackboardUpdateSession;
import com.elluminate.sas.BlackboardUrlResponse;
import com.elluminate.sas.ObjectFactory;

@Service
public class SessionWSDaoImpl implements SessionWSDao {
	
	private SASWebServiceOperations sasWebServiceOperations;
	
	@Autowired
	public void setSasWebServiceOperations(SASWebServiceOperations sasWebServiceOperations)
	{
		this.sasWebServiceOperations = sasWebServiceOperations;
	}

	@Override
	public BlackboardSessionResponse createSession(ConferenceUser user, SessionForm sessionForm) {
		final BlackboardSetSession setSession = new BlackboardSetSession();
        setSession.setCreatorId(user.getEmail());
        setSession.setSessionName(sessionForm.getSessionName());
        setSession.setStartTime(sessionForm.getStartTime().getMillis());
        setSession.setEndTime(sessionForm.getEndTime().getMillis());
        setSession.setBoundaryTime(sessionForm.getBoundaryTime());

        //TODO spring-security authz check
//        if (fullAccess) {
//            setSession.setMaxTalkers(sessionForm.getMaxTalkers());
//            setSession.setMaxCameras(sessionForm.getMaxCameras());
//            setSession.setMustBeSupervised(sessionForm.isMustBeSupervised());
//            setSession.setPermissionsOn(sessionForm.isPermissionsOn());
//            setSession.setRaiseHandOnEnter(sessionForm.isRaiseHandOnEnter());
//            final RecordingMode recordingMode = sessionForm.getRecordingMode();
//            if (recordingMode != null) {
//                setSession.setRecordingModeType(recordingMode.getBlackboardRecordingMode());
//            }
//            setSession.setHideParticipantNames(sessionForm.isHideParticipantNames());
//            setSession.setAllowInSessionInvites(sessionForm.isAllowInSessionInvites());
//        }

        final Object objSessionResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetSession", setSession);
        JAXBElement<BlackboardSessionResponseCollection> jaxbSessionResponse = (JAXBElement<BlackboardSessionResponseCollection>) objSessionResponse;
        
        final BlackboardSessionResponseCollection sessionResponses = jaxbSessionResponse.getValue();
        final BlackboardSessionResponse sessionResponse = DataAccessUtils.singleResult(sessionResponses.getSessionResponses());
        return sessionResponse;
		
	}
	
	@Override
	public String buildSessionUrl(Long sessionId, String displayName) {
		BlackboardBuildSessionUrl buildGuestUrlRequest = new BlackboardBuildSessionUrl();
        buildGuestUrlRequest.setSessionId(sessionId);
        buildGuestUrlRequest.setDisplayName(displayName);
        final Object objGuestUrlResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/BuildSessionUrl", buildGuestUrlRequest);
        JAXBElement<BlackboardUrlResponse> jaxbGuestUrlResponse = (JAXBElement<BlackboardUrlResponse>) objGuestUrlResponse;
        return  jaxbGuestUrlResponse.getValue().getUrl();
	}

	@Override
	public boolean createSessionTelephony(Long sessionId, BlackboardSetSessionTelephony telephony) {
		telephony.setSessionId(sessionId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/SetSessionTelephony", telephony));
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
		
		@SuppressWarnings("unchecked")
		Object obj = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSession", request); 
		JAXBElement<BlackboardSessionResponseCollection> responseCollection = (JAXBElement<BlackboardSessionResponseCollection>) obj;
		return responseCollection.getValue().getSessionResponses();
	}

	@Override
	public List<BlackboardSessionAttendanceResponse> getSessionAttendance(Long sessionId, Object startTime) {
		BlackboardListSessionAttendance request = new ObjectFactory().createBlackboardListSessionAttendance();
		BlackboardListSessionAttendanceResponseCollection responseCollection = (BlackboardListSessionAttendanceResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionAttendance", request);
		return responseCollection.getSessionAttendanceResponses();
	}

	@Override
	public List<BlackboardSessionTelephonyResponse> getSessionTelephony(Long sessionId) {
		BlackboardSessionTelephony request = new ObjectFactory().createBlackboardSessionTelephony();
		request.setSessionId(sessionId);
		final BlackboardSessionTelephonyResponseCollection response = (BlackboardSessionTelephonyResponseCollection) sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListSessionTelephony", request);
		return response.getSessionTelephonyResponses();
	}

	@Override
	public BlackboardSessionResponse updateSession(ConferenceUser user, SessionForm sessionForm) {
		final BlackboardSetSession updateSession = new ObjectFactory().createBlackboardSetSession();
		
		updateSession.setCreatorId(user.getEmail());
		
		updateSession.setSessionName(sessionForm.getSessionName());
		updateSession.setStartTime(sessionForm.getStartTime().getMillis());
		updateSession.setEndTime(sessionForm.getEndTime().getMillis());
		updateSession.setBoundaryTime(sessionForm.getBoundaryTime());

//        if (fullAccess) {
//        	updateSession.setMaxTalkers(sessionForm.getMaxTalkers());
//        	updateSession.setMaxCameras(sessionForm.getMaxCameras());
//        	updateSession.setMustBeSupervised(sessionForm.isMustBeSupervised());
//        	updateSession.setPermissionsOn(sessionForm.isPermissionsOn());
//        	updateSession.setRaiseHandOnEnter(sessionForm.isRaiseHandOnEnter());
//            final RecordingMode recordingMode = sessionForm.getRecordingMode();
//            if (recordingMode != null) {
//            	updateSession.setRecordingModeType(recordingMode.getBlackboardRecordingMode());
//            }
//            updateSession.setHideParticipantNames(sessionForm.isHideParticipantNames());
//            updateSession.setAllowInSessionInvites(sessionForm.isAllowInSessionInvites());
//        }
        
        final Object objSessionResponse = sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/UpdateSession", updateSession);
        @SuppressWarnings("unchecked")
		JAXBElement<BlackboardSessionResponseCollection> response = (JAXBElement<BlackboardSessionResponseCollection>) objSessionResponse;
        return DataAccessUtils.singleResult(response.getValue().getSessionResponses());
	}

	@Override
	public boolean deleteSession(Long sessionId) {
		BlackboardRemoveSession request = new ObjectFactory().createBlackboardRemoveSession();
		request.setSessionId(sessionId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveSession", request));
	}

	@Override
	public boolean clearSessionChairList(Long sessionId) {
		return clearSessionUserList(sessionId,true);
	}

	@Override
	public boolean clearSessionNonChairList(Long sessionId) {
		return clearSessionUserList(sessionId,false);
		
	}
	
	private boolean clearSessionUserList(Long sessionId, boolean isChairList) {
		BlackboardClearSessionUserList vo = new ObjectFactory().createBlackboardClearSessionUserList();
		vo.setSessionId(sessionId);
		return WSDaoUtils.isSuccessful(sasWebServiceOperations.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ClearSession"+ (isChairList ? "" : "Non"+"ChairList") , new ObjectFactory().createClearSessionChairList(vo)));
	}
}
