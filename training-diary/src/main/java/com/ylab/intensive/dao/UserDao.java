package com.ylab.intensive.dao;

import com.ylab.intensive.model.User;
import com.ylab.intensive.model.enums.Role;

import java.util.Optional;

public interface UserDao {
    boolean save(User user);

    Optional<User> findByEmail(String email);

    int size();
    Optional<User> updateUserRole(String email, Role newRole);
    void saveAction(String email, String action);
}
