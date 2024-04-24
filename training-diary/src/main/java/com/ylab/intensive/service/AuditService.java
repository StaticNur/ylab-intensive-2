package com.ylab.intensive.service;

import java.util.List;

/**
 * Service interface for audit operations.
 */
public interface AuditService {

    /**
     * Saves an action to the audit log.
     *
     * @param userId the ID of the user
     * @param action the action performed by the user
     */
    void saveAction(int userId, String action);

    /**
     * Retrieves audit information for a user.
     *
     * @param userId the ID of the user
     * @return a list of audit information
     */
    List<String> getAudit(int userId);
}
