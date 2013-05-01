/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 5/1/13 at 3:27 PM
 */
package org.jasig.portlet.blackboardvcportlet.validations.validators;

import org.jasig.portlet.blackboardvcportlet.service.SessionForm;
import org.jasig.portlet.blackboardvcportlet.validations.annotations.SessionBeginTimeRangeCheck;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SessionBeginTimeRangeCheckValidator implements ConstraintValidator<SessionBeginTimeRangeCheck, SessionForm>
{
	private static Logger logger = LoggerFactory.getLogger(SessionBeginTimeRangeCheckValidator.class);

	@Override
	public void initialize(SessionBeginTimeRangeCheck constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(SessionForm value, ConstraintValidatorContext context)
	{
		logger.info("isValid() with startTime = {} and value = {}", value.getStartTime(), value.getEndTime());

		DateTime startTime = value.getStartTime();
		DateTime endTime = value.getEndTime();
		long min = endTime.minusMinutes(15).getMillis();

		if (startTime.isBefore(min))
		{
			logger.info("Start DateTime is within range, so returning true.");
			return true;
		}
		logger.info("Start DateTime is NOT within range, so returning false.");
		return false;
	}
}
