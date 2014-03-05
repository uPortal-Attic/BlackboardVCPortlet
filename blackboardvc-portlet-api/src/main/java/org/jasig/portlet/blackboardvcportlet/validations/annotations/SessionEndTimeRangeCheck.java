/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 4/25/13 at 1:22 PM
 */
package org.jasig.portlet.blackboardvcportlet.validations.annotations;

import org.jasig.portlet.blackboardvcportlet.validations.validators.SessionEndTimeRangeCheckValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = SessionEndTimeRangeCheckValidator.class)
@Documented
public @interface SessionEndTimeRangeCheck
{
	String message() default "{org.jasig.portlet.blackboardvcportlet.validations.sessionendtimecheck.defaultmessage}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
