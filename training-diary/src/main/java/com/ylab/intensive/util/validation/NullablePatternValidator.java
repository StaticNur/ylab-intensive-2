package com.ylab.intensive.util.validation;

import com.ylab.intensive.util.validation.annotation.NullablePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullablePatternValidator implements ConstraintValidator<NullablePattern, String> {
    private String regexp;

    @Override
    public void initialize(NullablePattern constraintAnnotation) {
        regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches(regexp);
    }
}

