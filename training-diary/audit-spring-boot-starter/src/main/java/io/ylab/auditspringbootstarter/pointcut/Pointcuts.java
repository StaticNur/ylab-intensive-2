package io.ylab.auditspringbootstarter.pointcut;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Utility class containing pointcut definitions.
 * <p>
 * This class defines pointcuts for methods annotated with {@code @Auditable}.
 */
public class Pointcuts {
    /**
     * Pointcut definition for methods annotated with {@code @Auditable}.
     */
    @Pointcut("@annotation(io.ylab.auditspringbootstarter.annotation.Auditable)")
    public void callAuditableMethod() {
    }
}
