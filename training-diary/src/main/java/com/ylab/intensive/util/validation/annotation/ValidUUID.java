package com.ylab.intensive.util.validation.annotation;

import com.ylab.intensive.util.validation.ValidUUIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a parameter as requiring a valid UUID format.
 * This annotation is typically used in conjunction with parameter validation frameworks.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUUIDValidator.class)
public @interface ValidUUID {
    /**
     * The message to be returned when validation fails.
     *
     * @return The error message for invalid UUID format.
     */
    String message() default "Invalid UUID";

    /**
     * Groups targeted for validation.
     *
     * @return The validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload to attach to the constraint violation.
     *
     * @return The payload classes.
     */
    Class<? extends Payload>[] payload() default {};
}

