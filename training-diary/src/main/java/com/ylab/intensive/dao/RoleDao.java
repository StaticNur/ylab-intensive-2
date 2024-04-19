package com.ylab.intensive.dao;

import com.ylab.intensive.model.enums.Role;

public interface RoleDao {
    int findByName(Role role);
}
