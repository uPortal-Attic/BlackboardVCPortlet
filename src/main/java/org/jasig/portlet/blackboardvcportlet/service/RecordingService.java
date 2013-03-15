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

import com.elluminate.sas.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.blackboardvcportlet.dao.RecordingShortDao;
import org.jasig.portlet.blackboardvcportlet.dao.RecordingUrlDao;
import org.jasig.portlet.blackboardvcportlet.data.RecordingShort;
import org.jasig.portlet.blackboardvcportlet.data.RecordingUrl;
import org.jasig.portlet.blackboardvcportlet.data.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import javax.portlet.PortletPreferences;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service Class for handling Recording interactions
 * @author Richard Good
 */
@Service
public class RecordingService {
    
    protected final Log logger = LogFactory.getLog(SessionService.class);

	@Autowired
	private WebServiceTemplate webServiceTemplate;

	//private boolean isInit=false;
    //private BasicAuth user;
    
    @Autowired
    RecordingShortDao recordingDao;
    
    @Autowired
    RecordingUrlDao recordingUrlDao;
    
    @Autowired
    SessionService sessionService;

	/**
	 * Constructor
	 */
	public RecordingService()
	{
		super();
	}
	/**
     * Get the recordings for a session
     * @param sessionId
     * @return List<RecordingShort>
     */
    public List<RecordingShort> getRecordingsForSession(long sessionId)
    {
        return recordingDao.getAllSessionRecordings(sessionId);
    }
    
    /**
     * Get a specific recording
     * @param recordingId
     * @return RecordingShort
     */
    public RecordingShort getRecording(long recordingId)
    {
        RecordingShort recordingShort = recordingDao.getRecording(recordingId);
        recordingShort.setReadableFileSize(readableFileSize(recordingShort.getRecordingSize()));
        recordingShort.setCreatedDate(new Date(recordingShort.getCreationDate()));
        return recordingShort;
    }
    
    /**
     * Get the recordings for a user
     * @param uid
     * @return List<RecordingShort>
     */
    public List<RecordingShort> getRecordingsForUser(String uid)
    {
        List<RecordingShort> recordings = recordingDao.getRecordingsForUser(uid);
        RecordingUrl url;
        for (int i=0;i<recordings.size();i++)
        {  
            url = this.getRecordingUrl(recordings.get(i).getRecordingId());
            recordings.get(i).setRecordingUrl(url.getUrl());
            recordings.get(i).setCreatedDate(new Date(recordings.get(i).getCreationDate()));
            if ((recordings.get(i).getChairList()!=null&&recordings.get(i).getChairList().indexOf(uid+",")!=-1)||(recordings.get(i).getChairList()!=null&&recordings.get(i).getChairList().endsWith(uid)))
            {
                recordings.get(i).setCurrUserCanDelete(true);
            }
            else
            {
                recordings.get(i).setCurrUserCanDelete(false);
            }
            recordings.get(i).setReadableFileSize(readableFileSize(recordings.get(i).getRecordingSize()));
        }
        return recordings;
    }
    
    /**
     * Store a recording
     * @param recordingShort 
     */
    public void saveRecordingShort(RecordingShort recordingShort)
    {
        recordingDao.saveRecordingShort(recordingShort);
    }
    
    /**
     * Utility class for pretty output of file size
     * @param size
     * @return 
     */
    public static String readableFileSize(long size) {
        if(size <= 0) {
            return "0";
        }
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Get recordings as Admin
     * @return 
     */
    public List<RecordingShort> getRecordingsForAdmin()
    {
        List<RecordingShort> recordings = recordingDao.getAllRecordings();
        RecordingUrl url;
        for (int i=0;i<recordings.size();i++)
        {  
            url = this.getRecordingUrl(recordings.get(i).getRecordingId());
            recordings.get(i).setRecordingUrl(url.getUrl());
            recordings.get(i).setCreatedDate(new Date(recordings.get(i).getCreationDate()));
            recordings.get(i).setCurrUserCanDelete(true);
            recordings.get(i).setReadableFileSize(readableFileSize(recordings.get(i).getRecordingSize()));
        }
        return recordings;
    }
    
    /**
     * Gets the url for a recording
     * @param recordingId
     * @return 
     */
    public RecordingUrl getRecordingUrl(long recordingId)
    {
        List<RecordingUrl> recordingUrlList= recordingUrlDao.getRecordingUrls(recordingId);
        if (recordingUrlList.size()>0)
        {
            return recordingUrlList.get(0);
        }
        else
        {
            return null;
        }

    }
   
    /**
     * Delete a recording
     * @param prefs
     * @param recordingId
     * @throws Exception 
     */
    public void deleteRecording(PortletPreferences prefs,long recordingId) throws Exception
    {
        logger.debug("deleteRecording called");
        
        try
        {
			RemoveRecording removeRecording = new RemoveRecording();
			removeRecording.setRecordingId(recordingId);
			SuccessResponse successResponse = (SuccessResponse) webServiceTemplate.marshalSendAndReceive(removeRecording);
			logger.debug("removeRecording response:" + successResponse);
		}
        catch (Exception e)
        {
            logger.error("Exception caught calling Collaborate API",e);
            throw e;
        }
        recordingUrlDao.deleteRecordingUrls(recordingId);
        logger.debug("Deleted recordingUrl");
             
        recordingDao.deleteRecordingShort(recordingId);
        logger.debug("Deleted recordingShort");
    }
   
    /**
     * Updates the local recordings cache from Collaborate for a particular session
     * @param user
     * @param sessionId
     * @return 
     */
    public List<RecordingShort> updateSessionRecordings(BasicAuth user,long sessionId)
    {
        List<RecordingShort> recordingList = new ArrayList<RecordingShort>();
        try
        {
			ListRecordingShort listRecordingShort = new ListRecordingShort();
			listRecordingShort.setSessionId(sessionId);
			ListRecordingShortResponseCollection listRecordingShortResponseCollection = (ListRecordingShortResponseCollection)webServiceTemplate.marshalSendAndReceive(listRecordingShort);
			List<RecordingShortResponse> recordingShortResponses = listRecordingShortResponseCollection.getRecordingShortResponses();

			RecordingShort recordingShort;
			RecordingUrl recordingUrl;
			Session session = sessionService.getSession(sessionId);
			for (RecordingShortResponse shortResponse : recordingShortResponses)
			{
				recordingShort = new RecordingShort();
				recordingShort.setCreationDate(shortResponse.getCreationDate());
				recordingShort.setRecordingId(shortResponse.getRecordingId());
				recordingShort.setRecordingSize(shortResponse.getRecordingSize());
				recordingShort.setRoomName(shortResponse.getRoomName());
				recordingShort.setSessionId(shortResponse.getSessionId());
				recordingShort.setChairList(session.getChairList());
				recordingShort.setNonChairList(session.getNonChairList());
				logger.debug("initialised recording for recording id:" + recordingShort.getRecordingId());
				recordingDao.saveRecordingShort(recordingShort);
				logger.debug("stored recording short");
				logger.debug("getting url for recording");

				BuildRecordingUrl buildRecordingUrl = new BuildRecordingUrl();
				buildRecordingUrl.setRecordingId(recordingShort.getRecordingId());
				UrlResponse urlResponse = (UrlResponse) webServiceTemplate.marshalSendAndReceive(buildRecordingUrl);

				recordingUrl = new RecordingUrl();
				recordingUrl.setRecordingId(shortResponse.getRecordingId());
				recordingUrl.setUrl(urlResponse.getUrl());
				recordingUrl.setLastUpdated(new Date());
				logger.debug("initialised recording for recording url:" + recordingUrl.getUrl());
				recordingUrlDao.saveRecordingUrl(recordingUrl);
				logger.debug("Stored recording url");
				recordingList.add(recordingShort);
			}
		}
        catch (Exception e)
        {
            logger.error("Exception caught refreshing recordings",e);
        }
        return recordingList;
    }
}
