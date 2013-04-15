package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.Map;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import com.elluminate.sas.BlackboardSessionResponse;

public abstract class AbstractWSIT {
	
	BlackboardSessionResponse session;
	SessionForm form;
	ConferenceUser user;
	
	
	ConferenceUser buildUser() {
		ConferenceUser user = new ConferenceUser() {

			private static final long serialVersionUID = 1L;
			private String username = "test";
			private String email = "my-test2@example.com";
			private long id = 1;
			
			@Override
			public void setDisplayName(String displayName) {
				username = displayName;
				
			}
			
			@Override
			public long getUserId() {
				return id;
			}
			
			@Override
			public String getEmail() {
				return email;
			}
			
			@Override
			public String getDisplayName() {
				return username;
			}
			
			@Override
			public Map<String, String> getAttributes() {
				return null;
			}
		};
		return user;
		
	}

	SessionForm buildSession() {
		SessionForm newForm = new SessionForm();
		newForm.setAllowInSessionInvites(false);
		newForm.setBoundaryTime(0);
		newForm.setHideParticipantNames(false);
		newForm.setMaxCameras(1);
		newForm.setMaxTalkers(1);
		newForm.setMustBeSupervised(false);
		newForm.setNewSession(true);
		newForm.setPermissionsOn(false);
		newForm.setRaiseHandOnEnter(false);
		newForm.setSessionName("Test session");
		
		//start date/time
		newForm.setStartDate(new DateMidnight());
		newForm.setStartHour(0);
		newForm.setStartMinute(0);
		newForm.setStartTime((new DateTime()).plusHours(2).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0));
		
		//end date/time
		newForm.setEndDate(new DateMidnight());
		newForm.setEndHour(1);
		newForm.setEndMinute(0);
		newForm.setEndTime((new DateTime()).plusHours(3).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0));
		
		return newForm;
	}
}
