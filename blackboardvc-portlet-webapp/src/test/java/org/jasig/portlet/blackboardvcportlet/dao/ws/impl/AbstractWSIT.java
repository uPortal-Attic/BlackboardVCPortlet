package org.jasig.portlet.blackboardvcportlet.dao.ws.impl;

import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.jasig.portlet.blackboardvcportlet.data.SessionTelephony;
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
            public String getUniqueId() {
                return email;
            }

            @Override
            public void setEmail(String email) {
            }

            @Override
            public Set<String> getAdditionalEmails() {
                return null;
            }

            @Override
            public String getInvitationKey() {
                return null;
            }

            @Override
            public boolean isExternal() {
                return false;
            }

			@Override
			public String getBlackboardUniqueId() {
				 return getUniqueId();
			}
		};
		return user;
		
	}

	SessionForm buildSession() {
		SessionForm newForm = new SessionForm();
		newForm.setAllowInSessionInvites(false);
		newForm.setBoundaryTime(30);
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
		newForm.setStartTime(new DateTime(new Long("1370181600000")));
		
		//end date/time
		newForm.setEndDate(new DateMidnight());
		newForm.setEndHour(1);
		newForm.setEndMinute(0);
		newForm.setEndTime((new DateTime(new Long("1370185200000"))));
		
		return newForm;
	}
	
	SessionTelephony buildSessionTelephony() {
		SessionTelephony tel = new SessionTelephony() {
			private static final long serialVersionUID = 1L;

			@Override
			public Session getSession() {
				return null;
			}

			@Override
			public String getChairPhone() {
				return "1234567899";
			}

			@Override
			public String getChairPIN() {
				return "456789";
			}

			@Override
			public String getNonChairPhone() {
				return "1234567899";
			}

			@Override
			public String getNonChairPIN() {
				return "456789";
			}

			@Override
			public String getSessionSIPPhone() {
				return "1234567899";
			}

			@Override
			public String getSessionPIN() {
				return "123789";
			}

			@Override
			public boolean isPhone() {
				return false;
			}

			@Override
			public long getTelephonyId() {
				return 1;
			}

            @Override
            public Long getSessionId() {
                return Long.parseLong("1");
            }
		};
		return tel;
	}
}
