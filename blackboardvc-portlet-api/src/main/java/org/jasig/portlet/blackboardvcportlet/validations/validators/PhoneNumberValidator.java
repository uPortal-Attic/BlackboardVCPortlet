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
package org.jasig.portlet.blackboardvcportlet.validations.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.validations.annotations.PhoneNumber;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        
    }
 
    private static Pattern phonePattern;

    static {
        StringBuilder patternSB = new StringBuilder();
        patternSB.append("(1-\\d{3}-\\d{3}-\\d{4})");
        patternSB.append("|(1 \\d{3} \\d{3} \\d{4})");
        patternSB.append("|(1 \\(\\d{3}\\) \\d{3} \\d{4})");
        patternSB.append("|(1 \\(\\d{3}\\) \\d{3}-\\d{4})");
        patternSB.append("|(\\d{3}-\\d{3}-\\d{4})");
        patternSB.append("|(\\d{3} \\d{3} \\d{4})");
        patternSB.append("|(\\(\\d{3}\\) \\d{3}-\\d{4})");
        patternSB.append("|(\\d{3}\\.\\d{3}\\.\\d{4})");
        phonePattern = Pattern.compile(patternSB.toString());
    }
    
    /**
     * From Blackboard, here are valid values:
     * 1-xxx-xxx-xxxx
     * 1 xxx xxx xxxx
     * 1 (xxx) xxx xxxx
     * 1 (xxx) xxx-xxxx
     * xxx-xxx-xxxx
     * xxx xxx xxxx
     * (xxx) xxx-xxxx
     * xxx.xxx.xxxx
     * 
     * Returns true if it is empty (null or "") or if it matches one of the above patterns
     */
    @Override
    public boolean isValid(String testValue, ConstraintValidatorContext context) {
        if(StringUtils.isNotEmpty(testValue)) {
            Matcher matcher = phonePattern.matcher(testValue);
            return matcher.matches();
        } 
        return true;
    }

}
