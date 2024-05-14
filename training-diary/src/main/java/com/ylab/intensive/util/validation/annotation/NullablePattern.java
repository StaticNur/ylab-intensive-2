package com.ylab.intensive.util.validation.annotation;

import com.ylab.intensive.util.validation.NullablePatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to specify a regular expression pattern that allows null values.
 * This annotation can be applied to fields and indicates that the field value, if present,
 * must match the specified regular expression pattern.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NullablePatternValidator.class)
public @interface NullablePattern {
    /**
     * The regular expression pattern to be matched.
     *
     * @return The regular expression pattern.
     */
    String regexp();

    /**
     * The error message to be used if the value does not match the pattern.
     *
     * @return The error message.
     */
    String message() default "Значение не соответствует шаблону";

    /**
     * Groups to which this constraint belongs.
     *
     * @return An array of group classes.
     */
    Class<?>[] groups() default {};

    /**
     * Payload associated with this constraint.
     *
     * @return An array of payload classes.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @NullablePattern} annotations on the same element.
     *
     * @see NullablePattern
     */
    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NullablePattern[] value();
    }
}

