package com.ylab.intensive.repository;

import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.entity.Audit;

import java.util.List;

/**
 * Interface for audit data access operations.
 */
public interface AuditDao {

    /**
     * Retrieves a list of audit actions performed by a userId.
     * <p>
     * This method fetches a list of audit actions associated with the specified user ID,
     * optionally paginated using the provided {@link Pageable} object.
     * </p>
     *
     * @param userId   the ID of the user whose audit actions are to be retrieved.
     * @param pageable the pagination information for the result set.
     * @return a list of {@link Audit} objects representing the user's audit actions.
     */
    List<Audit> getUserActions(int userId, Pageable pageable);

    /**
     * Inserts a user action for a given user ID.
     *
     * @param userId the ID of the user
     * @param action the action performed by the user
     */
    void insertUserAction(int userId, String action);
}
