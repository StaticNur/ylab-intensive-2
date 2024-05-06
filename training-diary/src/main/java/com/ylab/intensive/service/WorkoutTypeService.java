package com.ylab.intensive.service;

import com.ylab.intensive.model.entity.WorkoutType;

import java.util.List;

/**
 * Service interface for managing workout types and related operations.
 * <p>
 * This interface defines methods to perform various operations related to workout types,
 * including saving, updating, finding, and deleting workout types.
 * </p>
 *
 * @since 1.0
 */
public interface WorkoutTypeService {
    /**
     * Saves a new workout type for the specified user.
     *
     * @param userId   The ID of the user for whom to save the workout type.
     * @param typeName The name of the workout type to save.
     * @return The saved workout type entity.
     */
    WorkoutType saveType(int userId, String typeName);

    /**
     * Finds all workout types associated with a specific user.
     *
     * @param userId The ID of the user whose workout types are to be retrieved.
     * @return A list of workout types associated with the user.
     */
    List<WorkoutType> findByUserId(int userId);

    /**
     * Updates the type of a workout with the specified ID.
     *
     * @param workoutId The ID of the workout to update.
     * @param oldType   The current type of the workout.
     * @param newType   The new type to assign to the workout.
     */
    void updateType(int workoutId, String oldType, String newType);

    /**
     * Retrieves a workout type by its name.
     *
     * @param name The name of the workout type to retrieve.
     * @return The workout type with the specified name, or null if no such workout type exists.
     */
    WorkoutType findByName(String name);

    /**
     * Retrieves a workout type by the name and the user ID.
     *
     * @param userId   The ID of the user associated with the workout type.
     * @param typeName The name of the workout type to retrieve.
     * @return The workout type with the specified name and associated with the specified user ID,
     * or null if no such workout type exists.
     */
    WorkoutType findTypeByUserId(int userId, String typeName);

    /**
     * Deletes all workout types associated with a specific user.
     *
     * @param userId The ID of the user whose workout types are to be deleted.
     */
    void delete(int userId);
}
