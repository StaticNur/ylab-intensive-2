package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.RoleDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.RoleService;

public class RoleServiceImpl implements RoleService {

    @Inject
    private RoleDao roleDao;

    @Override
    public int getIdByName(Role role) {
        return roleDao.findByName(role);
    }
}
