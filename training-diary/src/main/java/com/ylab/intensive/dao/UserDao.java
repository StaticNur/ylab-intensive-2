package com.ylab.intensive.dao;

import com.ylab.intensive.model.User;
import com.ylab.intensive.model.enums.Role;

import java.util.Optional;

/**
 * The UserDao interface provides methods to interact with user data in the database.
 */
public interface UserDao {
    /**
     * Saves a user to the database.
     *
     * @param user The user to save
     * @return true if the user was successfully saved, false otherwise
     */
    boolean save(User user);

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find
     * @return An Optional containing the user if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Gets the size of the user database.
     *
     * @return The size of the user database
     */
    int getSize();

    /**
     * Updates the role of a user.
     *
     * @param email   The email of the user to update
     * @param newRole The new role for the user
     * @return An Optional containing the updated user if found, otherwise empty
     */
    Optional<User> updateUserRole(String email, Role newRole);

    /**
     * Saves an action performed by a user.
     *
     * @param email  The email of the user who performed the action
     * @param action The action to save
     */
    void saveAction(String email, String action);
}
