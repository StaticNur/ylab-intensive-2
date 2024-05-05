package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.repository.UserDao;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.in.security.JwtTokenService;
import com.ylab.intensive.in.security.JwtUserDetailsService;
import com.ylab.intensive.service.AuditService;
import com.ylab.intensive.service.RoleService;
import com.ylab.intensive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the UserManagementService interface providing methods for managing user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * User DAO.
     * This DAO is responsible for data access operations related to users.
     */
    private final UserDao userDao;

    /**
     * Service for role-related operations.
     */
    private final RoleService roleService;

    /**
     * Service for audit-related operations.
     */
    private final AuditService auditService;

    /**
     * Authorized User Session.
     * This session represents the currently authorized user.
     */
    private final JwtTokenService jwtTokenService;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Override
    @Timed
    @Loggable
    @Transactional
    public User registerUser(RegistrationDto registrationDto) {
        userDao.findByEmail(registrationDto.getEmail())
                .ifPresent(u -> {
                    throw new RegistrationException("Такой пользователь уже существует!");
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
    @Auditable
    @Loggable
    @Timed
    public JwtResponse login(LoginDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {
            throw new InvalidInputException("Обязательно должны быть поля email и password");
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginDto.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().equals(loginDto.getPassword())) {
            throw new AuthorizeException("Incorrect password.");
        }
        String accessToken = jwtTokenService.createAccessToken(userDetails);
        String refreshToken = jwtTokenService.createRefreshToken(userDetails);

        return new JwtResponse(loginDto.getEmail(), accessToken, refreshToken);
    }

    @Override
    @Auditable
    @Loggable
    @Timed
    @Transactional
    public User changeUserPermissions(String uuidStr, ChangeUserRightsDto changeUserRightsDto) {
        Role role = changeUserRightsDto.newRole();//getRole(roleStr);
        int roleId = roleService.getIdByName(role);
        boolean isChange = userDao.updateUserRole(convertToUUID(uuidStr), roleId);
        if (isChange) {
            User user = userDao.findByUUID(convertToUUID(uuidStr))
                    .orElseThrow(() -> new NotFoundException("Пользователь с uuid = " + uuidStr + " не существует!"));
            return user;
        } else throw new ChangeUserPermissionsException("Failed to change user role");
    }

    @Override
    @Auditable
    @Loggable
    @Timed
    public AuditDto getAudit(String email, Pageable pageable) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("There is no user with this login in the database."));
        AuditDto auditDto = new AuditDto();
        List<Audit> audit = auditService.getAudit(user.getId(), pageable);
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
    @Auditable
    @Loggable
    @Timed
    public List<User> getAllUser() {
        return userDao.findAll();
    }

    @Override
    @Loggable
    @Timed
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * Converts the provided string representation of a UUID into a UUID object.
     *
     * @param uuidStr The string representation of the UUID to convert.
     * @return The UUID object corresponding to the input string.
     * @throws InvalidUUIDException If the input string is not a valid UUID format.
     */
    private UUID convertToUUID(String uuidStr) {
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(e.getMessage());
        }
    }
}
