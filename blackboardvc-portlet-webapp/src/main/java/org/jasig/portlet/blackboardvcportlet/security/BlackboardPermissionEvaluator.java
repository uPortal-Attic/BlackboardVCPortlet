package org.jasig.portlet.blackboardvcportlet.security;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class BlackboardPermissionEvaluator implements PermissionEvaluator {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private ConferenceUserService conferenceUserService;
    private Map<Class<Object>, PermissionTester<Object>> permissionTesters;
    
    @Autowired
    public void setConferenceUserService(ConferenceUserService conferenceUserService) {
        this.conferenceUserService = conferenceUserService;
    }

    @Autowired
    public void setPermissionTesters(Set<PermissionTester<Object>> permissionTesters) {
        final Builder<Class<Object>, PermissionTester<Object>> testersBuilder = ImmutableMap.builder();
        
        for (final PermissionTester<Object> permissionTester : permissionTesters) {
            testersBuilder.put(permissionTester.getDomainObjectType(), permissionTester);
        }
        
        this.permissionTesters = testersBuilder.build();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        logger.debug("{} - {} - {}", authentication, targetDomainObject, permission);
        
        final ConferenceUser user = this.conferenceUserService.getConferenceUser(authentication);
        
        for (final Map.Entry<Class<Object>, PermissionTester<Object>> permissionTesterEntry : this.permissionTesters.entrySet()) {
            final Class<?> testerType = permissionTesterEntry.getKey();
            if (testerType.isAssignableFrom(targetDomainObject.getClass())) {
                final PermissionTester<Object> tester = permissionTesterEntry.getValue();
                return tester.hasPermission(user, targetDomainObject, permission);
            }
        }
        
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        logger.error("UNSUPPORTED {} - {} - {} - {}", authentication, targetId, targetType, permission);
        //TODO need to decide on what targetType should be
        throw new UnsupportedOperationException();
    }
}
