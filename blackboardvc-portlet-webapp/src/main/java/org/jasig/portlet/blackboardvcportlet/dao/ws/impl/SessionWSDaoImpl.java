package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.jasig.portlet.blackboardvcportlet.dao.ws.SessionWSDao;
import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.RecordingMode;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;

import com.elluminate.sas.BlackboardBuildSessionUrl;
import com.elluminate.sas.BlackboardSessionResponse;
import com.elluminate.sas.BlackboardSessionResponseCollection;
import com.elluminate.sas.BlackboardSetSession;
import com.elluminate.sas.BlackboardUrlResponse;

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
	public void createSessionTelephony(int sessionId, Object telephony) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSessions(int userId, int groupingId, int sessionId,
			int creatorId, Object startTime, Object endTime, String sessionName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List getSessionAttendance(int sessionId, Object startTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getSessionTelephony(int sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSession(Object session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSession(int sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSessionChairList(int sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSessionNonChairList(int sessionId) {
		// TODO Auto-generated method stub
		
	}
}
