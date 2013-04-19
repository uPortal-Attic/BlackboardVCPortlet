package org.jasig.portlet.blackboardvcportlet.service.impl;

import org.jasig.portlet.blackboardvcportlet.service.RecordingService;
import org.springframework.stereotype.Service;

@Service("recordingService")
public class RecordingServiceImpl implements RecordingService {

    @Override
    public void updateSessionRecordings(long sessionId) {
        throw new UnsupportedOperationException("updateSessionRecordings(" + sessionId + ")");
    }
}
