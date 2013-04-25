/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/25/13 at 12:46 PM
 */
package org.jasig.portlet.blackboardvcportlet.validations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class QuarterHourCheckValidator implements ConstraintValidator<QuarterHourCheck, Integer>
{
	private static final Logger logger = LoggerFactory.getLogger(QuarterHourCheckValidator.class);

	@Override
	public void initialize(QuarterHourCheck constraintAnnotation)
	{
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context)
	{
		logger.debug("isValid() called with value = {}", value);
		if (value == null)
		{
			logger.debug("Null, so returning true.");
			return true;
		}

		if (value == 0 || value == 15 || value == 30 || value == 45)
		{
			logger.debug("Returning true.");
			return true;
		}
		logger.debug("Returning false.");
		return false;
	}
}
