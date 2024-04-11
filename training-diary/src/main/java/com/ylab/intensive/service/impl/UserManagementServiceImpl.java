package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.exception.ChangeUserPermissionsException;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.UserManagementService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserManagementService interface providing methods for managing user-related operations.
 */
public class UserManagementServiceImpl implements UserManagementService {
    @Inject
    private UserDao userDao;
    @Inject
    private Session authorizedUser;

    @Override
    public boolean registerUser(String email, String password, String roleStr) {
        Role role = getRole(roleStr);
        Optional<User> userMaybeExist = userDao.findByEmail(email);
        if (userMaybeExist.isPresent()) {
            throw new RegisterException("Такой пользователь уже существует!");
        }
        int sizeDB = userDao.getSize() + 1;
        User user = new User(sizeDB, email, password, new ArrayList<>(), new ArrayList<>(), role);
        return userDao.save(user);
    }

    @Override
    public Optional<User> login(String email, String password) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isPresent()) {
            String userPassword = user.get().getPassword();
            if (userPassword.equals(password)) {
                authorizedUser.setAttribute("authorizedUser", entityToDto(user.get()));

                saveAction("Пользователь " + email + " авторизовался");

                return Optional.of(user.get());
            }
        }
        return Optional.empty();
    }

    @Override
    public void logout() {
        saveAction("Пользователь разлогинился");
        authorizedUser.clearSession();
    }

    @Override
    public Optional<UserDto> changeUserPermissions(String email, String roleStr) {
        Role role = getRole(roleStr);
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");

        if (userFromAttribute.getRole().equals(Role.ADMIN)) {
            Optional<User> user = userDao.updateUserRole(email, role);
            if (user.isPresent()) {
                saveAction("Пользователь изменил роль на: " + role);

                return Optional.of(entityToDto(user.get()));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<String> getAudit() {
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");
        Optional<User> user = userDao.findByEmail(userFromAttribute.getEmail());

        return user.map(User::getAction).orElse(Collections.emptyList());
    }

    @Override
    public void saveAction(String action) {
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");
        userDao.saveAction(userFromAttribute.getEmail(), action);
    }

    private Role getRole(String roleStr) {
        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new ChangeUserPermissionsException(e.getMessage());
        }
        return role;
    }

    private UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }
}
