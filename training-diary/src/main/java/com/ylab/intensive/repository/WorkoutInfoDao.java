package com.ylab.intensive.repository;

import com.ylab.intensive.model.entity.WorkoutInfo;

import java.util.Optional;

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
     * @return a WorkoutInfo containing workout information
     */
    Optional<WorkoutInfo> findByWorkoutId(int workoutId);

    /**
     * Delete workout additional information by workout ID.
     *
     * @param workoutId the ID of the workout
     */
    void delete(int workoutId);
}
