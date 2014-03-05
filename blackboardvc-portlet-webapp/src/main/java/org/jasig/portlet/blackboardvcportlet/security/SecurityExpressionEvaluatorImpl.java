package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityExpressionEvaluatorImpl implements SecurityExpressionEvaluator, ApplicationContextAware {
    private final SimpleSecurityExpressionHandler expressionHandler = new SimpleSecurityExpressionHandler();

    public final void setExpressionParser(ExpressionParser expressionParser) {
        expressionHandler.setExpressionParser(expressionParser);
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        expressionHandler.setRoleHierarchy(roleHierarchy);
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        expressionHandler.setApplicationContext(applicationContext);
    }

    @Override
    public boolean authorize(String expression) {
        return authorize(expression, null);
    }

    @Override
    public boolean authorize(String expression, Map<String, Object> variables) {
        Expression accessExpression = expressionHandler.getExpressionParser().parseExpression(expression);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final EvaluationContext evaluationContext = expressionHandler.createEvaluationContext(authentication, variables);
        
        return ExpressionUtils.evaluateAsBoolean(accessExpression, evaluationContext);
    }
    
}
