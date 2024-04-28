package com.ylab.intensive.service;

import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {

    /**
     * Registers a new user with the specified email, password, and role.
     *
     * @return true if the user is successfully registered, false otherwise
     */
    User registerUser(RegistrationDto registrationDto);

    /**
     * Logs in the user with the specified email and password.
     *
     * @return An optional containing the user information if login is successful, empty otherwise
     */
    JwtResponse login(LoginDto loginDto);

    /**
     * Changes the role of the user with the specified email address.
     *
     * @return An optional containing the updated user information if successful, empty otherwise
     */
    User changeUserPermissions(String uuidStr, ChangeUserRightsDto changeUserRightsDto);

    /**
     * Retrieves the audit log.
     *
     * @return The list of audit log entries
     */
    AuditDto getAudit(String email);

    /**
     * Retrieves all User.
     *
     * @return List of User.
     */
    List<User> getAllUser();

    /**
     * Retrieves a user by email.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the user if found, otherwise an empty {@link Optional}
     */
    Optional<User> findByEmail(String email);
}
