package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserDao interface.
 * This class provides methods to interact with user data in the database.
 */
public class UserDaoImpl implements UserDao {

    /**
     * The list that represents the user database.
     */
    private final List<User> userDB = new ArrayList<>();

    @Override
    public boolean save(User user) {
        return userDB.add(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDB.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public int getSize() {
        return userDB.size();
    }

    @Override
    public Optional<User> updateUserRole(String email, Role newRole) {
        return findByEmail(email).map(user -> {
            user.setRole(newRole);
            return user;
        });
    }

    @Override
    public void saveAction(String email, String action) {
        userDB.stream()
                .filter(u -> u.getEmail().equals(email))
                .forEach(u -> u.getAction().add(action));
    }

    @Override
    public List<User> findAll() {
        return userDB;
    }

}
