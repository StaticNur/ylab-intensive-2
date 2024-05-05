package com.ylab.intensive.repository;

import com.ylab.intensive.model.entity.WorkoutType;

import java.util.List;
import java.util.Optional;

/**
 * Interface for workout type data access operations.
 */
public interface WorkoutTypeDao {

    /**
     * Saves a new workout type for a user.
     *
     * @param userId the ID of the user.
     * @param type   the name of the workout type to save.
     * @return the saved workout type object.
     */
    WorkoutType saveType(int userId, String type);

    /**
     * Updates the name of an existing workout type.
     *
     * @param userId  the ID of the user.
     * @param oldType the old name of the workout type.
     * @param newType the new name of the workout type.
     */
    void updateType(int userId, String oldType, String newType);

    /**
     * Retrieves all workout types associated with a user.
     *
     * @param userId the ID of the user.
     * @return a list of workout types associated with the user.
     */
    List<WorkoutType> findByUserId(int userId);

    /**
     * Deletes all workout types associated with a user.
     *
     * @param userId the ID of the user.
     */
    void delete(int userId);

    /**
     * Retrieves a workout type by its name.
     *
     * @param typeName the name of the workout type.
     * @return an optional containing the workout type if found, or empty if not found.
     */
    Optional<WorkoutType> findByName(String typeName);

    /**
     * Retrieves a workout type by its name.
     *
     * @param typeName the name of the workout type.
     * @param userId the ID of the user.
     * @return an optional containing the workout type if found, or empty if not found.
     */
    Optional<WorkoutType> findTypeByUserId(int userId, String typeName);
}
