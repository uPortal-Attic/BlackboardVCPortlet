package org.jasig.portlet.blackboardvcportlet.security;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jasig.portlet.blackboardvcportlet.data.ConferenceUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.util.ClassUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * Delegates to other domain object specific {@link PermissionTester} instances
 * 
 * @author Eric Dalquist
 */
public class DelegatingPermissionEvaluator implements PermissionEvaluator {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private ConferenceUserService conferenceUserService;
    private Map<Class<Object>, PermissionTester<Object>> permissionTesters;
    private Map<Class<? extends Object>, PermissionTester<Object>> permissionTesterResolutionCache;
    
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
        
        this.permissionTesterResolutionCache = new ConcurrentHashMap<Class<? extends Object>, PermissionTester<Object>>();
        this.permissionTesters = testersBuilder.build();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        logger.debug("Does {} have {} on {}", authentication, permission, targetDomainObject);
        
        final ConferenceUser user = this.conferenceUserService.getConferenceUser(authentication);
        
        final Class<? extends Object> targetType = targetDomainObject.getClass();
        final PermissionTester<Object> tester = this.resolvePermissionTester(targetType);
        return tester.hasPermission(user, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetTypeName, Object permission) {
        logger.debug("Does {} have {} on {} - {}", authentication, permission, targetTypeName, targetId);
        
        final ConferenceUser user = this.conferenceUserService.getConferenceUser(authentication);
        
        final Class<?> targetType = this.getTargetType(targetTypeName);
        final PermissionTester<Object> tester = this.resolvePermissionTester(targetType);
        return tester.hasPermissionById(user, targetId, permission);
    }
    
    private Class<?> getTargetType(String targetTypeName) {
        try {
            return ClassUtils.forName(targetTypeName, null);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Permission target '" + targetTypeName + "' does not exist", e);
        }
    }
    
    private PermissionTester<Object> resolvePermissionTester(Class<? extends Object> targetType) {
        PermissionTester<Object> tester = permissionTesterResolutionCache.get(targetType);
        if (tester != null) {
            return tester;
        }
        
        for (final Map.Entry<Class<Object>, PermissionTester<Object>> permissionTesterEntry : this.permissionTesters.entrySet()) {
            final Class<?> testerType = permissionTesterEntry.getKey();
            if (testerType.isAssignableFrom(targetType)) {
                tester = permissionTesterEntry.getValue();
                break;
            }
        }
        
        if (tester == null) {
            tester = FalsePermissionTester.INSTANCE;
        }
        permissionTesterResolutionCache.put(targetType, tester);
        
        return tester;
    }
    
    private static final class FalsePermissionTester implements PermissionTester<Object> {
        public static final FalsePermissionTester INSTANCE = new FalsePermissionTester();
        
        @Override
        public Class<Object> getDomainObjectType() {
            return Object.class;
        }

        @Override
        public boolean hasPermission(ConferenceUser user, Object targetDomainObject, Object permission) {
            return false;
        }

        @Override
        public boolean hasPermissionById(ConferenceUser user, Serializable targetId, Object permission) {
            return false;
        }
    }
}
