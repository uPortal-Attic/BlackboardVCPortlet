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
package org.jasig.portlet.blackboardvcportlet.service;

import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.data.RecordingUrl;
import javax.portlet.PortletPreferences;
import java.util.List;

/**
 * Service Class for handling Recording interactions
 * @author Richard Good
 */
public interface RecordingService
{
	/**
     * Get the recordings for a session
     * @param sessionId Long
     * @return List<RecordingShort>
     */
    public List<SessionRecording> getRecordingsForSession(long sessionId);
    
    /**
     * Get a specific recording
     * @param recordingId Long
     * @return RecordingShort
     */
    public SessionRecording getRecording(long recordingId);
    
    /**
     * Get the recordings for a user
     * @param uid String
     * @return List<RecordingShort>
     */
    public List<SessionRecording> getRecordingsForUser(String uid);
    
    /**
     * Store a recording
     * @param recordingShort RecordingShort
     */
    public void saveRecordingShort(SessionRecording recordingShort);
    
    /**
     * Get recordings as Admin
     * @return List<RecordingShort>
     */
    public List<SessionRecording> getRecordingsForAdmin();
    
    /**
     * Gets the url for a recording
     * @param recordingId Long
     * @return RecordingUrl
     */
    public RecordingUrl getRecordingUrl(long recordingId);
    
    /**
     * Delete a recording
     * @param prefs PortletPreferences
     * @param recordingId Long
     * @throws Exception 
     */
    public void deleteRecording(PortletPreferences prefs, long recordingId) throws Exception;
    
    /**
     * Updates the local recordings cache from Collaborate for a particular session
     *
	 * @param sessionId Long
	 * @return List<RecordingShort>
     */
    public List<SessionRecording> updateSessionRecordings(long sessionId);
}
