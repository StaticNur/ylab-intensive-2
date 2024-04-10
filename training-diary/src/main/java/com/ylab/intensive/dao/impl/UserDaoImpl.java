package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.model.User;
import com.ylab.intensive.model.enums.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
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

}
