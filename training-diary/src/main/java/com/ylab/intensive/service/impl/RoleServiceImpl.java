package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.RoleDao;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.RoleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

/**
 * Implementation class for {@link RoleService}.
 */
@ApplicationScoped
@NoArgsConstructor
public class RoleServiceImpl implements RoleService {
    /**
     * This DAO is responsible for data access operations related to role.
     */
    private RoleDao roleDao;

    @Inject
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public int getIdByName(Role role) {
        return roleDao.findByName(role);
    }
}
