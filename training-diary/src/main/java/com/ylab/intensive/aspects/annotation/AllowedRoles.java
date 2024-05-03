package com.ylab.intensive.aspects.annotation;

import com.ylab.intensive.model.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the roles allowed to access a method.
 * <p>
 * This annotation can be applied to methods to specify which roles are allowed to invoke them.
 * It is evaluated at runtime to enforce role-based access control.
 * </p>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AllowedRoles {

    /**
     * Specifies the roles allowed to access the method.
     *
     * @return an array of Role enums representing the allowed roles.
     */
    Role[] value();
}
