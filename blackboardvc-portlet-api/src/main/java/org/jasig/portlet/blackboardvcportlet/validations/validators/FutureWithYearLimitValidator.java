package org.jasig.portlet.blackboardvcportlet.validations.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jasig.portlet.blackboardvcportlet.validations.annotations.FutureWithYearLimit;
import org.joda.time.DateTime;

public class FutureWithYearLimitValidator implements ConstraintValidator<FutureWithYearLimit, DateTime> {

	@Override
	public void initialize(FutureWithYearLimit constraintAnnotation) {
	}

	@Override
	public boolean isValid(DateTime startTime, ConstraintValidatorContext context) {
		DateTime maxTime = DateTime.now().plusYears(1);
		return startTime.isBefore(maxTime);
	}

}
