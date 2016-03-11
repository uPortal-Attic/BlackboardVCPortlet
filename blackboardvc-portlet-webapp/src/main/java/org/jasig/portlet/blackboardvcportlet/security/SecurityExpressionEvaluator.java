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
