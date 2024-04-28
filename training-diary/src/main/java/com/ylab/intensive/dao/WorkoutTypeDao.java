package com.ylab.intensive.dao;

import com.ylab.intensive.model.entity.WorkoutType;

import java.util.List;
import java.util.Optional;

/**
 * Interface for workout type data access operations.
 */
public interface WorkoutTypeDao {

    /**
     * Saves the type of a workout.
     *
     * @param type The type of the workout to save
     */
    WorkoutType saveType(int userId, String type);

    /**
     * Updates the type of a workout.
     *
     * @param workoutId The workout ID containing the type to update
     * @param oldType   The old type of the workout
     * @param newType   The new type of the workout
     */
    void updateType(int workoutId, String oldType, String newType);

    /**
     * Finds workout types by workout ID.
     *
     * @param userId the ID of the workout
     * @return a set of workout types
     */
    List<WorkoutType> findByUserId(int userId);

    /**
     * Deletes workout types by workout ID.
     *
     * @param workoutId the ID of the workout
     */
    void delete(int workoutId);

    Optional<WorkoutType> findById(int id);

    Optional<WorkoutType> findByType(String typeName);
}
