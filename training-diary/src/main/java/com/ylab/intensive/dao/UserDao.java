package com.ylab.intensive.dao;

import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.User;

import java.util.List;
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
    boolean save(User user, int roleId);

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find
     * @return An Optional containing the user if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Updates the user's role in the database by the specified identifier.
     *
     * @param email  the user email
     * @param roleId the role id
     * @return the updated user
     * @throws DaoException if an SQL exception occurs
     */
    boolean updateUserRole(String email, int roleId);

    /**
     * Finds all users in the database.
     *
     * @return A list of all workouts in the database
     */
    List<User> findAll();
}
