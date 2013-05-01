/**
 * @author Brad Leege <leege@doit.wisc.edu>
 * Created on 5/1/13 at 3:26 PM
 */
package org.jasig.portlet.blackboardvcportlet.validations.annotations;

import org.jasig.portlet.blackboardvcportlet.validations.validators.SessionBeginTimeRangeCheckValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = SessionBeginTimeRangeCheckValidator.class)
@Documented
public @interface SessionBeginTimeRangeCheck
{
	String message() default "{org.jasig.portlet.blackboardvcportlet.validations.sessionbegintimecheck.defaultmessage}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

