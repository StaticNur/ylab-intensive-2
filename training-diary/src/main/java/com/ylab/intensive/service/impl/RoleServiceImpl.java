package com.ylab.intensive.service.impl;

import com.ylab.intensive.repository.RoleDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation class for {@link RoleService}.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    /**
     * This DAO is responsible for data access operations related to role.
     */
    private final RoleDao roleDao;

    @Override
    public int getIdByName(Role role) {
        Integer id = roleDao.findByName(role);
        if (id == null){
            throw new NotFoundException("Такой роли нет в базе данных.");
        }
        return id;
    }
}
