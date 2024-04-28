package com.ylab.intensive.dao;

import com.ylab.intensive.model.entity.Audit;

import java.util.List;

/**
 * Interface for audit data access operations.
 */
public interface AuditDao {

    /**
     * Retrieves a list of user actions by user ID.
     *
     * @param userId the ID of the user
     * @return a list of user actions
     */
    List<Audit> getUserActions(int userId);

    /**
     * Inserts a user action for a given user ID.
     *
     * @param userId the ID of the user
     * @param action the action performed by the user
     */
    void insertUserAction(int userId, String action);
}
