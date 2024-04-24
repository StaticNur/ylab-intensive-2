package com.ylab.intensive.dao;

import java.util.Map;

/**
 * Interface for workout additional information data access operations.
 */
public interface WorkoutInfoDao {

    /**
     * Saves additional information for a workout.
     *
     * @param workoutId The workout id to save the information for
     * @param title     The title of the information
     * @param info      The information to save
     */
    void saveWorkoutInfo(int workoutId, String title, String info);

    /**
     * Updates the additional information of a workout.
     *
     * @param workoutId The workout id containing the information to update
     * @param title     The title of the information to update
     * @param info      The new information
     */
    void updateWorkoutInfo(int workoutId, String title, String info);

    /**
     * Finds workout additional information by workout ID.
     *
     * @param workoutId the ID of the workout
     * @return a map containing workout information
     */
    Map<String, String> findByWorkoutId(int workoutId);

    /**
     * Delete workout additional information by workout ID.
     *
     * @param workoutId the ID of the workout
     */
    void delete(int workoutId);
}
