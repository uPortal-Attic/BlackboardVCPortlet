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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.validations.annotations.SessionStartIsBeforeEndTimeCheck;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionStartIsBeforeEndTimeCheckValidator implements ConstraintValidator<SessionStartIsBeforeEndTimeCheck, SessionForm> {
	private static Logger logger = LoggerFactory.getLogger(SessionStartIsBeforeEndTimeCheckValidator.class);

	@Override
	public void initialize(SessionStartIsBeforeEndTimeCheck constraintAnnotation)
	{
		
	}

	@Override
	public boolean isValid(SessionForm value, ConstraintValidatorContext context)
	{
		DateTime startTime = value.getStartTime();
		DateTime endTime = value.getEndTime();

		if (startTime.isBefore(endTime)) {
			logger.info("All is well");
			return true;
		}
		logger.info("Start time was after end time, or the same, for shame");
		return false;
	}
}

