package com.ylab.intensive.aspects.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The Auditable annotation is used to mark methods that are subject to auditing.
 * Methods annotated with this annotation can be tracked and logged
 * for auditing purposes.
 * <p>
 * Note that this annotation works at runtime, so information about annotated
 * methods can be obtained and used during program execution.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Auditable {
}
