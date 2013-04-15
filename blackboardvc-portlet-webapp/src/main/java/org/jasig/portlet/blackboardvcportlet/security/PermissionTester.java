package org.jasig.portlet.blackboardvcportlet.security;

import java.io.Serializable;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;

public interface PermissionTester<T> {
    Class<T> getDomainObjectType();
    
    boolean hasPermission(ConferenceUser user, T targetDomainObject, Object permission);
    
    boolean hasPermissionById(ConferenceUser user, Serializable targetId, Object permission);
}
