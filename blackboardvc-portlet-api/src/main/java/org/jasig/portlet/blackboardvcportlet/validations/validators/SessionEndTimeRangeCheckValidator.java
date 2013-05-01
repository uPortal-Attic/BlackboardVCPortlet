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
		long min = startTime.plusMinutes(15).getMillis();
		long max = startTime.plusYears(1).getMillis();

		if (endTime.isAfter(min) && endTime.isBefore(max))
		{
			logger.info("End DateTime is within range, so returning true.");
			return true;
		}
		logger.info("End DateTime is NOT within range, so returning false.");
		return false;
	}
}
