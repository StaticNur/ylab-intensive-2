package com.ylab.intensive.service;

import com.ylab.intensive.exception.AccessDeniedException;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for user-related operations.
 * <p>
 * This interface defines methods for user registration, email, changing user permissions,
 * retrieving audit logs, and accessing user information.
 * </p>
 *
 * @since 1.0
 */
public interface UserService {

    /**
     * Registers a new user with the provided registration data.
     *
     * @param registrationDto The registration data of the user.
     * @return The registered user.
     */
    User registerUser(RegistrationDto registrationDto);

    /**
     * Logs in a user using the provided email credentials.
     *
     * @param loginDto The email credentials of the user.
     * @return The JWT response containing access and refresh tokens.
     */
    JwtResponse login(LoginDto loginDto);

    /**
     * Changes the permissions of a user identified by the UUID.
     *
     * @param uuidStr             The UUID of the user.
     * @param changeUserRightsDto The new user permissions.
     * @return The updated user.
     */
    User changeUserPermissions(String uuidStr, ChangeUserRightsDto changeUserRightsDto);

    /**
     * Retrieves audit logs for a user specified by their email address.
     *
     * @param email    The email address of the user.
     * @param pageable The pagination information.
     * @return The audit logs for the user.
     */
    AuditDto getAudit(String email, Pageable pageable);

    /**
     * Retrieves a list of all users.
     *
     * @return The list of all users.
     */
    List<User> getAllUser();

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An optional containing the user if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);

    /**
     * Updates the user's access and refresh tokens using the provided refresh token.
     *
     * @param refreshToken The refresh token to use for updating the tokens.
     * @return The updated JWT response containing the new access and refresh tokens.
     * @throws AccessDeniedException If the refresh token is invalid or expired.
     */
    JwtResponse updateToken(String refreshToken);
}

