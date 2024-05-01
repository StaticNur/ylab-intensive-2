package com.ylab.intensive.dao;

import com.ylab.intensive.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The UserDao interface provides methods to interact with user data in the database.
 */
public interface UserDao {

    /**
     * Saves a user along with their associated role.
     *
     * @param user   the user to be saved.
     * @param roleId the ID of the role associated with the user.
     * @return the saved user object.
     */
    User save(User user, int roleId);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve.
     * @return an optional containing the user if found, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Updates the role of a user specified by their UUID.
     *
     * @param uuid   the UUID of the user whose role is to be updated.
     * @param roleId the ID of the new role.
     * @return {@code true} if the role was updated successfully, {@code false} otherwise.
     */
    boolean updateUserRole(UUID uuid, int roleId);

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users in the system.
     */
    List<User> findAll();

    /**
     * Retrieves a user by their UUID.
     *
     * @param uuid the UUID of the user to retrieve.
     * @return an optional containing the user if found, or empty if not found.
     */
    Optional<User> findByUUID(UUID uuid);
}
