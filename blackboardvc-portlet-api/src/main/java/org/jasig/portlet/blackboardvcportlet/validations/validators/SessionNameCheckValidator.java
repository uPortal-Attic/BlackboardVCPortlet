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
/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 5/1/13 at 2:28 PM
 */
package org.jasig.portlet.blackboardvcportlet.validations.validators;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.validations.annotations.SessionNameCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SessionNameCheckValidator implements ConstraintValidator<SessionNameCheck, String>
{
	private static final Logger logger = LoggerFactory.getLogger(SessionNameCheckValidator.class);

	@Override
	public void initialize(SessionNameCheck constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context)
	{
		logger.debug("isValid() called with value = {}", value);
		if (StringUtils.isEmpty(value))
		{
			logger.debug("Null, so returning false.");
			return false;
		}

		// Test For Size
		if (value.length() > 255)
		{
			logger.debug("The string is longer than 255 characters, returning false.");
			return false;
		}

		// Test for illegal characters
		if (value.contains("<") || value.contains("&") || value.contains("#") || value.contains("%"))
		{
			logger.debug("Illegal character found, so returning false.");
			return false;
		}

		logger.debug("Passed all the tests, returning true.");
		return true;
	}
}
