package org.jasig.portlet.blackboardvcportlet.security;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;

public interface PermissionTester<T> {
    Class<T> getDomainObjectType();
    
    boolean hasPermission(ConferenceUser user, T targetDomainObject, Object permission);
}
