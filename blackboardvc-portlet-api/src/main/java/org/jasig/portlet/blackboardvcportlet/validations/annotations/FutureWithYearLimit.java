package org.jasig.portlet.blackboardvcportlet.validations.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.jasig.portlet.blackboardvcportlet.validations.validators.FutureWithYearLimitValidator;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = FutureWithYearLimitValidator.class)
@Documented
public @interface FutureWithYearLimit {
	String message() default "{org.jasig.portlet.blackboardvcportlet.validations.futurewithlimitcheck.defaultmessage}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
