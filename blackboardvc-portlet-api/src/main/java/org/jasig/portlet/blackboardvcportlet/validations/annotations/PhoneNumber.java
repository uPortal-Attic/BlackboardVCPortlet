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

import org.jasig.portlet.blackboardvcportlet.validations.validators.PhoneNumberValidator;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface PhoneNumber {
    
    String message() default "{org.jasig.portlet.blackboardvcportlet.validations.phonenumbercheck.defaultmessage}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
