package com.ylab.intensive.service.impl;

import com.ylab.intensive.repository.UserDao;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.service.security.impl.JwtUserDetailsService;
import com.ylab.intensive.service.AuditService;
import com.ylab.intensive.service.RoleService;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.converter.Converter;
import io.ylab.auditspringbootstarter.service.UserFinder;
import io.ylab.loggingspringbootstarter.annotation.Loggable;
import io.ylab.loggingspringbootstarter.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the UserManagementService interface providing methods for managing user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserFinder {
    /**
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
     * The service responsible for the token
     */
    private final JwtTokenService jwtTokenService;

    /**
     * The service responsible for managing user details for JWT authentication.
     */
    private final JwtUserDetailsService jwtUserDetailsService;

    /**
     * The password encoder used for encoding and decoding passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The converter used for converting one type of object to another.
     */
    private final Converter converter;

    @Override
    @Timed
    @Loggable
    @CacheEvict(value = "AllUser", allEntries = true)
    @Transactional
    public User registerUser(RegistrationDto registrationDto) {
        userDao.findByEmail(registrationDto.getEmail())
                .ifPresent(u -> {
                    throw new RegistrationException("Такой пользователь уже существует!");
                });
        int roleId = roleService.getIdByName(registrationDto.getRole());
        User user = generateNewUser(registrationDto);
        return userDao.save(user, roleId);
    }

    @Override
    @Loggable
    @Timed
    public JwtResponse login(LoginDto loginDto) {
        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {
            throw new InvalidInputException("Обязательно должны быть поля email и password");
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginDto.getEmail());
        if (!passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            throw new AuthorizeException("The password for this email is incorrect.");
        }

        String accessToken = jwtTokenService.createAccessToken(userDetails);
        String refreshToken = jwtTokenService.createRefreshToken(userDetails);

        return new JwtResponse(loginDto.getEmail(), accessToken, refreshToken);
    }

    @Override
    @Loggable
    @Timed
    @CacheEvict(value = "AllUser", allEntries = true)
    @Transactional
    public User changeUserPermissions(String uuidStr, ChangeUserRightsDto changeUserRightsDto) {
        Role role = changeUserRightsDto.newRole();
        int roleId = roleService.getIdByName(role);
        UUID uuid = converter.convert(uuidStr, UUID::fromString, "Invalid UUID");
        boolean isChange = userDao.updateUserRole(uuid, roleId);
        if (isChange) {
            return userDao.findByUUID(uuid)
                    .orElseThrow(() -> new NotFoundException("Пользователь с uuid = " + uuidStr + " не существует!"));
        } else throw new DaoException("Failed to change user role. Invalid uuid");
    }

    @Override
    @Loggable
    @Timed
    public AuditDto getAudit(String email, Pageable pageable) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("There is no user with this email in the database."));

        AuditDto auditDto = new AuditDto();
        auditDto.setUuid(user.getUuid());
        auditDto.setEmail(user.getEmail());
        auditDto.setRole(user.getRole());

        List<Audit> audit = auditService.getAudit(user.getId(), pageable);
        Map<LocalDateTime, List<String>> actionMap = audit.stream()
                .collect(Collectors.groupingBy(
                        Audit::getDateOfAction,
                        TreeMap::new,
                        Collectors.mapping(Audit::getAction, Collectors.toList())
                ));
        auditDto.setAction(actionMap);
        return auditDto;
    }

    @Override
    @Loggable
    @Timed
    @Cacheable(value = "AllUser")
    public List<User> getAllUser() {
        return userDao.findAll();
    }

    @Override
    @Loggable
    @Timed
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public int findIdByEmail(String email) {
        return findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new NotFoundException("User with email = " + email + " does not exist!"));
    }

    @Override
    @Loggable
    @Timed
    public JwtResponse updateToken(String refreshToken) {
        return jwtTokenService.refreshUserToken(refreshToken);
    }

    private User generateNewUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());
        return user;
    }
}
