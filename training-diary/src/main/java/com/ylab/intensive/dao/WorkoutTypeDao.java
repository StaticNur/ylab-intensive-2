package com.ylab.intensive.dao;

import java.util.Set;

/**
 * Interface for workout type data access operations.
 */
public interface WorkoutTypeDao {

    /**
     * Saves the type of a workout.
     *
     * @param workoutId The workout ID to save the type for
     * @param type      The type of the workout to save
     */
    void saveType(int workoutId, String type);

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
     * @param workoutId the ID of the workout
     * @return a set of workout types
     */
    Set<String> findByWorkoutId(int workoutId);

    /**
     * Deletes workout types by workout ID.
     *
     * @param workoutId the ID of the workout
     */
    void delete(int workoutId);
}
