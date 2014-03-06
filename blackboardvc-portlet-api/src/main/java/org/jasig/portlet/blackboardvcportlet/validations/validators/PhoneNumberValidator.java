package org.jasig.portlet.blackboardvcportlet.validations.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.blackboardvcportlet.validations.annotations.PhoneNumber;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        
    }
 
    private static Pattern phonePattern;

    static {
        StringBuilder patternSB = new StringBuilder();
        patternSB.append("(1-\\d{3}-\\d{3}-\\d{4})");
        patternSB.append("|(1 \\d{3} \\d{3} \\d{4})");
        patternSB.append("|(1 \\(\\d{3}\\) \\d{3} \\d{4})");
        patternSB.append("|(1 \\(\\d{3}\\) \\d{3}-\\d{4})");
        patternSB.append("|(\\d{3}-\\d{3}-\\d{4})");
        patternSB.append("|(\\d{3} \\d{3} \\d{4})");
        patternSB.append("|(\\(\\d{3}\\) \\d{3}-\\d{4})");
        patternSB.append("|(\\d{3}\\.\\d{3}\\.\\d{4})");
        phonePattern = Pattern.compile(patternSB.toString());
    }
    
    /**
     * From Blackboard, here are valid values:
     * 1-xxx-xxx-xxxx
     * 1 xxx xxx xxxx
     * 1 (xxx) xxx xxxx
     * 1 (xxx) xxx-xxxx
     * xxx-xxx-xxxx
     * xxx xxx xxxx
     * (xxx) xxx-xxxx
     * xxx.xxx.xxxx
     * 
     * Returns true if it is empty (null or "") or if it matches one of the above patterns
     */
    @Override
    public boolean isValid(String testValue, ConstraintValidatorContext context) {
        if(StringUtils.isNotEmpty(testValue)) {
            Matcher matcher = phonePattern.matcher(testValue);
            return matcher.matches();
        } 
        return true;
    }

}
