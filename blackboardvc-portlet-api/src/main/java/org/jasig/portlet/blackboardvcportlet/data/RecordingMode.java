package org.jasig.portlet.blackboardvcportlet.data;

public enum RecordingMode {
    MANUAL(1),
    AUTOMATIC(2),
    DISABLE(3);
    
    public static RecordingMode resolveRecordingMode(long blackboardRecordingMode) {
        for (final RecordingMode mode : RecordingMode.values()) {
            if (blackboardRecordingMode == mode.getBlackboardRecordingMode()) {
                return mode;
            }
        }
        
        return null;
    }
    
    private final long blackboardRecordingMode;
    
    private RecordingMode(long blackboardRecordingMode) {
        this.blackboardRecordingMode = blackboardRecordingMode;
    }
    
    public long getBlackboardRecordingMode() {
        return blackboardRecordingMode;
    }
    
    public String getName() {
        return this.name();
    }
}
