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
package org.jasig.portlet.blackboardvcportlet.data;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * @author Eric Dalquist
 */
public interface Session extends Serializable {

    long getSessionId();

    long getEntityVersion();
    
    DateTime getLastUpdated();

    String getGuestUrl();
    
    
    //********** properties below are direct copies from the BlackboardSessionResponse **********//

    long getBbSessionId();
    
    String getSessionName();

    DateTime getStartTime();

    DateTime getEndTime();
    
    ConferenceUser getCreator();
    
    Presentation getPresentation();

    int getBoundaryTime();

    AccessType getAccessType();

    boolean isRecordings();

    String getChairNotes();

    String getNonChairNotes();

    //TODO missing groupingList

    boolean isOpenChair();
    
    boolean isPermissionsOn();

    boolean isMustBeSupervised();

    RecordingMode getRecordingMode();

    int getMaxTalkers();

    int getMaxCameras();

    boolean isRaiseHandOnEnter();

    int getReserveSeats();
    
    boolean isSecureSignOn();

    long getVersionId();

    boolean isAllowInSessionInvites();

    boolean isHideParticipantNames();
    
    void setLaunchUrl(String launchUrl);
    
    String getLaunchUrl();

	String getTimeUntilJoin();
	DateTime getStartTimeWithBoundaryTime ();

	String getTimeFancyText(DateTime from, DateTime to);
}