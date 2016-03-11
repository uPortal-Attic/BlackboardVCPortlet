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
