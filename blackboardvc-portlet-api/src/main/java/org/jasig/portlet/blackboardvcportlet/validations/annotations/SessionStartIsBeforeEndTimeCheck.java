package org.jasig.portlet.blackboardvcportlet.validations.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.jasig.portlet.blackboardvcportlet.validations.validators.SessionStartIsBeforeEndTimeCheckValidator;

@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = SessionStartIsBeforeEndTimeCheckValidator.class)
@Documented
public @interface SessionStartIsBeforeEndTimeCheck {
	String message() default "{org.jasig.portlet.blackboardvcportlet.validations.SessionStartIsBeforeEndTimeCheck.defaultmessage}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}