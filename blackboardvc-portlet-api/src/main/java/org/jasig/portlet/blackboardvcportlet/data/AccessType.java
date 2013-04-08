package org.jasig.portlet.blackboardvcportlet.data;

public enum AccessType {
    PRIVATE(1),
    RESTRICTED(2),
    PUBLIC(3);
    
    public static AccessType resolveAccessType(long blackboardAccessType) {
        for (final AccessType mode : AccessType.values()) {
            if (blackboardAccessType == mode.getBlackboardAccessType()) {
                return mode;
            }
        }
        
        return null;
    }
    
    private final long blackboardAccessType;
    
    private AccessType(long blackboardAccessType) {
        this.blackboardAccessType = blackboardAccessType;
    }
    
    public long getBlackboardAccessType() {
        return blackboardAccessType;
    }
    
    public String getName() {
        return this.getName();
    }
}
