package com.ylab.intensive.util.validation.annotation;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.util.validation.RoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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

