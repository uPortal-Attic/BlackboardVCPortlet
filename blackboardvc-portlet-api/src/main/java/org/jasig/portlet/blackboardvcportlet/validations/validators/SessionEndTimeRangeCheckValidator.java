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
 * Created on 4/25/13 at 1:25 PM
 */
package org.jasig.portlet.blackboardvcportlet.validations.validators;

import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.validations.annotations.SessionEndTimeRangeCheck;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SessionEndTimeRangeCheckValidator implements ConstraintValidator<SessionEndTimeRangeCheck, SessionForm>
{
	private static Logger logger = LoggerFactory.getLogger(SessionEndTimeRangeCheckValidator.class);

	@Override
	public void initialize(SessionEndTimeRangeCheck constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(SessionForm value, ConstraintValidatorContext context)
	{
		logger.info("isValid() with startTime = {} and value = {}", value.getStartTime(), value.getEndTime());

		DateTime startTime = value.getStartTime();
		DateTime endTime = value.getEndTime();
		long max = startTime.plusYears(1).getMillis();

		if (endTime.isBefore(max))
		{
			logger.info("End DateTime is within range, so returning true.");
			return true;
		}
		logger.info("End DateTime is NOT within range, so returning false.");
		return false;
	}
}
