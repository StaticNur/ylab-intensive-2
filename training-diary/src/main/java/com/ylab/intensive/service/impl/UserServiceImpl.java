package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.service.AuditService;
import com.ylab.intensive.service.RoleService;
import com.ylab.intensive.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the UserManagementService interface providing methods for managing user-related operations.
 */
@ApplicationScoped
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * User DAO.
     * This DAO is responsible for data access operations related to users.
     */
    private UserDao userDao;

    /**
     * Service for role-related operations.
     */
    private RoleService roleService;

    /**
     * Service for audit-related operations.
     */
    private AuditService auditService;

    /**
     * Authorized User Session.
     * This session represents the currently authorized user.
     */
    private JwtTokenService jwtTokenService;

    @Inject
    public UserServiceImpl(UserDao userDao, RoleService roleService,
                           AuditService auditService, JwtTokenService jwtTokenService) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.auditService = auditService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    @Timed
    @Loggable
    public User registerUser(RegistrationDto registrationDto) {
        userDao.findByEmail(registrationDto.getEmail())
                .ifPresent(u -> {
                    throw new RegisterException("Такой пользователь уже существует!");
                });
        int roleId = roleService.getIdByName(registrationDto.getRole());
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        user.setRole(registrationDto.getRole());
        return userDao.save(user, roleId);
    }

    @Override
    @Timed
    @Loggable
    @Auditable
    public JwtResponse login(LoginDto loginDto) {
        User user = userDao.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("There is no user with this login in the database."));

        if (user.getPassword() != null && !user.getPassword().equals(loginDto.getPassword())) {
            throw new AuthorizeException("Incorrect password.");
        }

        String accessToken = jwtTokenService.createAccessToken(loginDto.getEmail(), user.getRole());
        String refreshToken = jwtTokenService.createRefreshToken(loginDto.getEmail(), user.getRole());
        jwtTokenService.authentication(accessToken);

        return new JwtResponse(loginDto.getEmail(), accessToken, refreshToken);
    }

    @Override
    @Timed
    @Loggable
    public User changeUserPermissions(String uuidStr, ChangeUserRightsDto changeUserRightsDto) {
        Role role = changeUserRightsDto.newRole();//getRole(roleStr);
        int roleId = roleService.getIdByName(role);
        boolean isChange = userDao.updateUserRole(convertToUUID(uuidStr), roleId);
        if (isChange) {
            User user = userDao.findByUUID(convertToUUID(uuidStr))
                    .orElseThrow(() -> new NotFoundException("Пользователь с uuid = " + uuidStr + " не существует!"));

            auditService.saveAction(user.getId(), "Пользователь изменил роль на: " + role);
            return user;
        } else throw new ChangeUserPermissionsException("Failed to change user role");
    }

    @Override
    @Timed
    @Loggable
    public AuditDto getAudit(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("There is no user with this login in the database."));
        AuditDto auditDto = new AuditDto();
        List<Audit> audit = auditService.getAudit(user.getId());
        auditDto.setUuid(user.getUuid());
        auditDto.setEmail(user.getEmail());
        auditDto.setRole(user.getRole());
        auditDto.setAction(audit.stream()
                .collect(Collectors.toMap(
                        Audit::getDateOfAction,
                        Audit::getAction
                )));
        return auditDto;
    }

    @Override
    @Timed
    @Loggable
    public List<User> getAllUser() {//TODO id
        auditService.saveAction(1, "Пользователь просмотрел все тренировки всех людей.");
        return userDao.findAll();
    }

    @Override
    @Timed
    @Loggable
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * Retrieves the ID of the currently authorized user.
     *
     * @return the ID of the authorized user
     * @throws NotFoundException if the authorized user is not found
     */
    private int getAuthorizedUserId(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email = "
                                                         + email + " does not exist!"));
        return user.getId();
    }

    private UUID convertToUUID(String uuidStr) {
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(e.getMessage());
        }
    }
}
