package com.ylab.intensive.service;

import com.ylab.intensive.dto.UserDto;
import com.ylab.intensive.model.User;

import java.util.List;
import java.util.Optional;

public interface UserManagementService {
    boolean registerUser(String email, String password, String role);
    Optional<UserDto> login(String email, String password);
    void logout();
    Optional<UserDto> changeUserPermissions(String email, String role);
    List<String>  getAudit();
    void saveAction(String action);
}
