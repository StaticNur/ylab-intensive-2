package com.ylab.intensive.service;

import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.dto.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing user-related operations.
 */
public interface UserManagementService {

    /**
     * Registers a new user with the specified email, password, and role.
     *
     * @param email    The email address of the user
     * @param password The password of the user
     * @param role     The role of the user (ADMIN or USER)
     * @return true if the user is successfully registered, false otherwise
     */
    boolean registerUser(String email, String password, String role);

    /**
     * Logs in the user with the specified email and password.
     *
     * @param email    The email address of the user
     * @param password The password of the user
     * @return An optional containing the user information if login is successful, empty otherwise
     */
    Optional<User> login(String email, String password);

    /**
     * Logs out the currently logged-in user.
     */
    void logout();

    /**
     * Changes the role of the user with the specified email address.
     *
     * @param email The email address of the user
     * @param role  The new role to be assigned to the user
     * @return An optional containing the updated user information if successful, empty otherwise
     */
    Optional<UserDto> changeUserPermissions(String email, String role);

    /**
     * Retrieves the audit log.
     *
     * @return The list of audit log entries
     */
    List<String> getAudit();

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
