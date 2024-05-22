package io.ylab.auditspringbootstarter.service;

/**
 * Functional interface for finding user ID by email.
 * <p>
 * This interface defines a method for retrieving the ID of a user based on their email address.
 */
@FunctionalInterface
public interface UserFinder {
    /**
     * Finds the ID of a user by their email address.
     *
     * @param email The email address of the user.
     * @return The ID of the user.
     */
    int findIdByEmail(String email);
}
