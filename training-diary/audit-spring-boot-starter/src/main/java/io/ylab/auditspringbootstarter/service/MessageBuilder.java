package io.ylab.auditspringbootstarter.service;

import org.aspectj.lang.JoinPoint;

/**
 * Functional interface for building audit messages.
 * <p>
 * This interface defines a method for generating audit messages based on a join point
 * and user email.
 */
@FunctionalInterface
public interface MessageBuilder {
    /**
     * Generates an audit message based on the join point and user email.
     *
     * @param jp    The join point representing the method invocation.
     * @param email The email of the user performing the action.
     * @return The generated audit message.
     */
    String generate(JoinPoint jp, String email);
}
