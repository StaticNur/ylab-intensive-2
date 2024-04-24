package com.ylab.intensive.service;

import java.util.Set;

/**
 * Service interface for workout type operations.
 */
public interface WorkoutTypeService {

    /**
     * Saves a workout type.
     *
     * @param workoutId the ID of the workout
     * @param typeName  the name of the workout type to save
     */
    void saveType(int workoutId, String typeName);

    /**
     * Finds workout types by workout ID.
     *
     * @param workoutId the ID of the workout
     * @return a set of workout types
     */
    Set<String> findByWorkoutId(int workoutId);

    /**
     * Updates a workout type.
     *
     * @param workoutId the ID of the workout
     * @param oldType   the old workout type
     * @param newType   the new workout type
     */
    void updateType(int workoutId, String oldType, String newType);

    /**
     * Deletes workout types by workout ID.
     *
     * @param workoutId the ID of the workout
     */
    void delete(int workoutId);
}
