package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Map;

import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SimpleSecurityExpressionHandler extends AbstractSecurityExpressionHandler<Map<String, Object>> {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    
    @Override
    protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, Map<String, Object> variables) {
        final StandardEvaluationContext evaluationContext = super.createEvaluationContextInternal(authentication, variables);
        
        if (variables != null) {
            for (final Map.Entry<String, Object> variableEntry : variables.entrySet()) {
                evaluationContext.setVariable(variableEntry.getKey(), variableEntry.getValue());
            }
        }
        
        return evaluationContext;
    }
    
    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, Map<String, Object> variables) {
        SimpleSecurityExpressionRoot root = new SimpleSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }

    private final class SimpleSecurityExpressionRoot extends SecurityExpressionRoot {
        public SimpleSecurityExpressionRoot(Authentication a) {
            super(a);
        }
    }
}
