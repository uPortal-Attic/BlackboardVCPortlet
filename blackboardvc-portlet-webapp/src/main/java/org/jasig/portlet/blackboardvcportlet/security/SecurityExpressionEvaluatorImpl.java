/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
