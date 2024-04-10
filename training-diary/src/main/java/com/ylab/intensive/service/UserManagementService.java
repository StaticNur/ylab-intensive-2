package com.ylab.intensive.service;

import com.ylab.intensive.model.User;

import java.util.List;

public interface UserManagementService {
    boolean registerUser(String email, String password, String role);
    User login(String email, String password);
    boolean logout();
    User changeUserPermissions(String email, String role);
    List<String>  getAudit();
}
