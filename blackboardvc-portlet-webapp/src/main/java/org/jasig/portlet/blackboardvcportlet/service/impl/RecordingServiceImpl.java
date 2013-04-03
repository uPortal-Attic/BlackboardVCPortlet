package org.jasig.portlet.blackboardvcportlet.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletPreferences;

import org.jasig.portlet.blackboardvcportlet.dao.SessionRecordingDao;
import org.jasig.portlet.blackboardvcportlet.dao.RecordingUrlDao;
import org.jasig.portlet.blackboardvcportlet.data.SessionRecording;
import org.jasig.portlet.blackboardvcportlet.data.RecordingShortImpl;
import org.jasig.portlet.blackboardvcportlet.data.RecordingUrl;
import org.jasig.portlet.blackboardvcportlet.data.RecordingUrlImpl;
import org.jasig.portlet.blackboardvcportlet.data.BlackboardSession;
import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.jasig.portlet.blackboardvcportlet.service.SessionService;
import org.jasig.portlet.blackboardvcportlet.service.util.SASWebServiceTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elluminate.sas.BuildRecordingUrl;
import com.elluminate.sas.ListRecordingShort;
import com.elluminate.sas.ListRecordingShortResponseCollection;
import com.elluminate.sas.ObjectFactory;
import com.elluminate.sas.RecordingShortResponse;
import com.elluminate.sas.RemoveRecording;
import com.elluminate.sas.SuccessResponse;
import com.elluminate.sas.UrlResponse;

@Service("recordingService")
public class RecordingServiceImpl implements RecordingService {
    private static final Logger logger = LoggerFactory.getLogger(RecordingService.class);

	@Autowired
	private SASWebServiceTemplate sasWebServiceTemplate;

	@Autowired
	private ObjectFactory objectFactory;

    @Autowired
    SessionRecordingDao recordingDao;
    
    @Autowired
    RecordingUrlDao recordingUrlDao;
    
    @Autowired
    SessionService sessionService;

	/**
	 * Constructor
	 */
	public RecordingServiceImpl()
	{
		super();
	}
	/**
     * Get the recordings for a session
     * @param sessionId Long
     * @return List<RecordingShort>
     */
    public List<SessionRecording> getRecordingsForSession(long sessionId)
    {
        return recordingDao.getAllSessionRecordings(sessionId);
    }
    
    /**
     * Get a specific recording
     * @param recordingId Long
     * @return RecordingShort
     */
    public SessionRecording getRecording(long recordingId)
    {
        SessionRecording recordingShort = recordingDao.getRecording(recordingId);
        recordingShort.setReadableFileSize(readableFileSize(recordingShort.getRecordingSize()));
        recordingShort.setCreatedDate(new Date(recordingShort.getCreationDate()));
        return recordingShort;
    }
    
    /**
     * Get the recordings for a user
     * @param uid String
     * @return List<RecordingShort>
     */
    public List<SessionRecording> getRecordingsForUser(String uid)
    {
        List<SessionRecording> recordings = recordingDao.getRecordingsForUser(uid);
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
     * @param recordingShort RecordingShort
     */
    public void saveRecordingShort(SessionRecording recordingShort)
    {
        recordingDao.saveRecordingShort(recordingShort);
    }

    /**
     * Get recordings as Admin
     * @return List<RecordingShort>
     */
    public List<SessionRecording> getRecordingsForAdmin()
    {
        List<SessionRecording> recordings = recordingDao.getAllRecordings();
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
     * @param recordingId Long
     * @return RecordingUrl
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
     * @param prefs PortletPreferences
     * @param recordingId Long
     * @throws Exception 
     */
    public void deleteRecording(PortletPreferences prefs, long recordingId) throws Exception
    {
        logger.debug("deleteRecording called");
        
        try
        {
			RemoveRecording removeRecording = objectFactory.createRemoveRecording();
			removeRecording.setRecordingId(recordingId);
			SuccessResponse successResponse = (SuccessResponse) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/RemoveRecording", removeRecording);
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
     *
	 * @param sessionId Long
	 * @return List<RecordingShort>
     */
    public List<SessionRecording> updateSessionRecordings(long sessionId)
    {
        List<SessionRecording> recordingList = new ArrayList<SessionRecording>();
        try
        {
			ListRecordingShort listRecordingShort = objectFactory.createListRecordingShort();
			listRecordingShort.setSessionId(sessionId);
			ListRecordingShortResponseCollection listRecordingShortResponseCollection = (ListRecordingShortResponseCollection)sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/ListRecordingShort", listRecordingShort);
			List<RecordingShortResponse> recordingShortResponses = listRecordingShortResponseCollection.getRecordingShortResponses();

			RecordingShortImpl recordingShort;
			RecordingUrl recordingUrl;
			BlackboardSession session = sessionService.getSession(sessionId);
			for (RecordingShortResponse shortResponse : recordingShortResponses)
			{
				recordingShort = new RecordingShortImpl();
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

				BuildRecordingUrl buildRecordingUrl = objectFactory.createBuildRecordingUrl();
				buildRecordingUrl.setRecordingId(recordingShort.getRecordingId());
				UrlResponse urlResponse = (UrlResponse) sasWebServiceTemplate.marshalSendAndReceiveToSAS("http://sas.elluminate.com/BuildRecordingUrl", buildRecordingUrl);

				recordingUrl = new RecordingUrlImpl();
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
    
    /**
     * Utility class for pretty output of file size
     * @param size Long
     * @return String
     */
    private static String readableFileSize(long size) {
        if(size <= 0) {
            return "0";
        }
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
