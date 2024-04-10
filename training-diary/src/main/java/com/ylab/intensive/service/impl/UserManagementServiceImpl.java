package com.ylab.intensive.service.impl;

import com.ylab.intensive.dto.UserDto;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.exception.changeUserPermissionsException;
import com.ylab.intensive.model.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.UserManagementService;

import java.util.Collections;
import java.util.List;

public class UserManagementServiceImpl implements UserManagementService {
    @Override
    public boolean registerUser(String email, String password, String roleStr) {
        try {
            Role role = Role.valueOf(roleStr);
        }catch (IllegalArgumentException e){
            throw new RegisterException(e.getMessage());
        }
        return true;
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public User changeUserPermissions(String username, String roleStr) {
        try {
            Role role = Role.valueOf(roleStr);
        }catch (IllegalArgumentException e){
            throw new changeUserPermissionsException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<String> getAudit() {
        return Collections.emptyList();
    }

    private UserDto entityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }
    private User dtoToEntity(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }
}
