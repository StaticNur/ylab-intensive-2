package com.ylab.intensive.util.validation;

import com.ylab.intensive.util.validation.annotation.NullablePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link NullablePattern} annotation.
 * This validator checks if a string value matches the specified regular expression pattern,
 * allowing the value to be null.
 */
public class NullablePatternValidator implements ConstraintValidator<NullablePattern, String> {
    private String regexp;

    /**
     * Initializes the validator with the regular expression pattern specified in the annotation.
     *
     * @param constraintAnnotation The {@link NullablePattern} annotation instance.
     */
    @Override
    public void initialize(NullablePattern constraintAnnotation) {
        regexp = constraintAnnotation.regexp();
    }

    /**
     * Validates if the given value matches the regular expression pattern.
     * The value is allowed to be null.
     *
     * @param value   The value to validate.
     * @param context The validation context.
     * @return {@code true} if the value is null or matches the pattern, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches(regexp);
    }
}


