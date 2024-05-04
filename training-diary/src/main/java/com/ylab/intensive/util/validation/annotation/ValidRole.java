package com.ylab.intensive.util.validation.annotation;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.util.validation.RoleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to specify valid roles for a field or parameter.
 * This annotation can be applied to fields or parameters of type {@link Role},
 * indicating that the value must be one of the specified allowed roles.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = RoleValidator.class)
public @interface ValidRole {
    String message() default "Недопустимая роль! Он должен быть либо ADMIN, либо USER.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Role[] allowedRoles();
}

