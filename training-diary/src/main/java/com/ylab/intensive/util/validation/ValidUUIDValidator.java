package com.ylab.intensive.util.validation;

import com.ylab.intensive.util.validation.annotation.ValidUUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator class for validating UUID format based on the {@link ValidUUID} annotation.
 */
public class ValidUUIDValidator implements ConstraintValidator<ValidUUID, String> {
    /**
     * Pattern to match the UUID format.
     */
    private static final Pattern UUID_PATTERN = Pattern
            .compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    /**
     * Validates whether the provided value matches the UUID format.
     *
     * @param value   The value to be validated.
     * @param context The context in which the constraint is evaluated.
     * @return {@code true} if the value is a valid UUID format, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        Matcher matcher = UUID_PATTERN.matcher(value);
        return matcher.matches();
    }
}

