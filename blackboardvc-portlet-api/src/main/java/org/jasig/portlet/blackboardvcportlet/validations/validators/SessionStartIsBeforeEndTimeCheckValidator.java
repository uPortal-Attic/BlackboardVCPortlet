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

