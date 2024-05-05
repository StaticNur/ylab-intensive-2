package com.ylab.intensive.repository;

import com.ylab.intensive.model.enums.Role;

/**
 * Interface for role data access operations.
 */
public interface RoleDao {

    /**
     * Finds a role ID by its name.
     *
     * @param role the {@link Role} enum value
     * @return the ID of the role
     */
    Integer findByName(Role role);
}
