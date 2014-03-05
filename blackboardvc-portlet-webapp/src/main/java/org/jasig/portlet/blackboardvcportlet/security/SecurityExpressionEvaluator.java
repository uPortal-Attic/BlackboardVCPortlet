package org.jasig.portlet.blackboardvcportlet.security;

import java.util.Map;

/**
 * Allows for execution of Spring-Security expressions directly in java code:
 * http://static.springsource.org/spring-security/site/docs/3.1.x/reference/el-access.html
 * 
 * @author Eric Dalquist
 */
public interface SecurityExpressionEvaluator {
    /**
     * @return Evaluate the specified spring-security expression
     */
    boolean authorize(String expression);
    
    /**
     * @return Evaluate the specified spring-security expression adding the data in the variables map to the spring expression context
     */
    boolean authorize(String expression, Map<String, Object> variables);
}
