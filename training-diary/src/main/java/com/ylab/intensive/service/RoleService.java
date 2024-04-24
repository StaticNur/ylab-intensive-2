package com.ylab.intensive.service;

import com.ylab.intensive.model.enums.Role;

/**
 * Service interface for role operations.
 */
public interface RoleService {

    /**
     * Retrieves the ID of a role by its name.
     *
     * @param role the role name
     * @return the ID of the role
     */
    int getIdByName(Role role);
}
