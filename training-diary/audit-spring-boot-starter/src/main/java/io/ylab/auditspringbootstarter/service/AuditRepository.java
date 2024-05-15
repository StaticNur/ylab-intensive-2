package io.ylab.auditspringbootstarter.service;

/**
 * Functional interface for auditing user actions.
 * <p>
 * This interface defines a method for inserting user actions into an audit repository.
 */
@FunctionalInterface
public interface AuditRepository {
    /**
     * Inserts a user action into the audit repository.
     *
     * @param id     The ID of the user.
     * @param action The action performed by the user.
     */
    void insertUserAction(int id, String action);
}
