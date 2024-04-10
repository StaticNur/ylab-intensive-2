package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.dao.impl.UserDaoImpl;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.dto.UserDto;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.exception.changeUserPermissionsException;
import com.ylab.intensive.model.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.security.Session;
import com.ylab.intensive.service.UserManagementService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserManagementServiceImpl implements UserManagementService {
    @Inject
    private UserDao userDao;
    @Inject
    private Session authorizedUser;

    @Override
    public boolean registerUser(String email, String password, String roleStr) {
        try {
            Role role = Role.valueOf(roleStr);
            Optional<User> userMaybeExist = userDao.findByEmail(email);
            if (userMaybeExist.isPresent()) {
                throw new RegisterException("Такой пользователь уже существует!");
            }
            int sizeDB = userDao.size() + 1;
            User user = new User(sizeDB, email, password, new ArrayList<>(), new ArrayList<>(), role);
            return userDao.save(user);
        } catch (IllegalArgumentException e) {
            throw new RegisterException(e.getMessage());
        }
    }

    @Override
    public Optional<UserDto> login(String email, String password) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isPresent()) {
            String userPassword = user.get().getPassword();
            if (userPassword.equals(password)) {
                authorizedUser.setAttribute("authorizedUser", user);
                return Optional.of(entityToDto(user.get()));
            }
        }
        return Optional.empty();
    }

    @Override
    public void logout() {
        authorizedUser.clearSession();
    }

    @Override
    public Optional<UserDto> changeUserPermissions(String email, String roleStr) {
        try {
            Role role = Role.valueOf(roleStr);
            Optional<User> user = userDao.updateUserRole(email, role);
            if (user.isPresent()) {
                return Optional.of(entityToDto(user.get()));
            }
        } catch (IllegalArgumentException e) {
            throw new changeUserPermissionsException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<String> getAudit() {
        Optional<User> objectUser = (Optional<User>) authorizedUser.getAttribute("authorizedUser");
        Optional<User> user = userDao.findByEmail(objectUser.get().getEmail());

        return user.map(User::getAction).orElse(Collections.emptyList());
    }

    @Override
    public void saveAction(String action) {
        Optional<User> user = (Optional<User>) authorizedUser.getAttribute("authorizedUser");
        user.ifPresent(u -> userDao.saveAction(u.getEmail(), action));
    }

    private UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private User dtoToEntity(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }
}
