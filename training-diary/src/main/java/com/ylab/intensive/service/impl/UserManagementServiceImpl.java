package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.dao.RoleDao;
import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.exception.ChangeUserPermissionsException;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.model.mapper.UserMapper;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.AuditService;
import com.ylab.intensive.service.RoleService;
import com.ylab.intensive.service.UserManagementService;
import com.ylab.intensive.service.WorkoutService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserManagementService interface providing methods for managing user-related operations.
 */
public class UserManagementServiceImpl implements UserManagementService {
    /**
     * User DAO.
     * This DAO is responsible for data access operations related to users.
     */
    @Inject
    private UserDao userDao;

    @Inject
    private RoleService roleService;

    @Inject
    private AuditService auditService;
    /**
     * Authorized User Session.
     * This session represents the currently authorized user.
     */
    @Inject
    private Session authorizedUser;
    @Inject
    private UserMapper userMapper;


    @Override
    public boolean registerUser(String email, String password, String roleStr) {
        Role role = getRole(roleStr);
        userDao.findByEmail(email)
                .ifPresent(u -> {
                    throw new RegisterException("Такой пользователь уже существует!");
                });
        int roleId = roleService.getIdByName(role);
        User user = User.builder()
                .email(email)
                .password(password)
                .workout(new ArrayList<>())
                .action(new ArrayList<>())
                .role(role)
                .build();
        return userDao.save(user, roleId);
    }

    @Override
    public Optional<User> login(String email, String password) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с email = " + email + " не существует!"));
        String userPassword = user.getPassword();
        if (userPassword.equals(password)) {
            authorizedUser.setAttribute("authorizedUser", userMapper.entityToDto(user));

            auditService.saveAction(user.getId(), "Пользователь " + email + " авторизовался");

            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public void logout() {
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");
        User user = userDao.findByEmail(userFromAttribute.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь с email = "
                                                         + userFromAttribute.getEmail() + " не существует!"));

        auditService.saveAction(user.getId(), "Пользователь разлогинился");
        authorizedUser.clearSession();
    }

    @Override
    public Optional<UserDto> changeUserPermissions(String email, String roleStr) {
        Role role = getRole(roleStr);
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");

        if (userFromAttribute.getRole().equals(Role.ADMIN)) {
            int roleId = roleService.getIdByName(role);
            boolean isChange = userDao.updateUserRole(email, roleId);
            if (isChange) {
                User user = userDao.findByEmail(email)
                        .orElseThrow(() -> new NotFoundException("Пользователь с email = " + email + " не существует!"));

                auditService.saveAction(user.getId(), "Пользователь изменил роль на: " + role);
                authorizedUser.removeAttribute("authorizedUser");
                authorizedUser.setAttribute("authorizedUser", userMapper.entityToDto(user));
                return Optional.of(userMapper.entityToDto(user));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<String> getAudit() {
        return auditService.getAudit(getAuthorizedUserId());
    }

    @Override
    public void saveAction(String action) {
        auditService.saveAction(getAuthorizedUserId(), action);
    }

    @Override
    public List<User> getAllUser() {
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");
        User user = userDao.findByEmail(userFromAttribute.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь с email = "
                                                         + userFromAttribute.getEmail() + " не существует!"));

        if (userFromAttribute.getRole().equals(Role.ADMIN)) {
            auditService.saveAction(user.getId(), "Пользователь просмотрел все тренировки всех людей.");
            return userDao.findAll();
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
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
    private int getAuthorizedUserId() {
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");
        User user = userDao.findByEmail(userFromAttribute.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь с email = "
                                                         + userFromAttribute.getEmail() + " не существует!"));
        return user.getId();
    }
}
