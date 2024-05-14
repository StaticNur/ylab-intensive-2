package com.ylab.intensive.service;

import com.ylab.intensive.model.entity.WorkoutInfo;

import java.util.Optional;

/**
 * Service interface for workout information operations.
 */
public interface WorkoutInfoService {

    /**
     * Saves workout information.
     *
     * @param workoutId the ID of the workout
     * @param title     the title of the information
     * @param info      the information to save
     */
    void saveWorkoutInfo(int workoutId, String title, String info);

    /**
     * Updates workout information.
     *
     * @param workoutId the ID of the workout
     * @param title     the title of the information
     * @param info      the new information
     */
    void updateWorkoutInfo(int workoutId, String title, String info);

    /**
     * Retrieves workout information by workout ID.
     *
     * @param workoutId the ID of the workout
     * @return a WorkoutInfo containing the workout information
     */
    Optional<WorkoutInfo> getInfoByWorkoutId(int workoutId);

    /**
     * Deletes workout information by workout ID.
     *
     * @param workoutId the ID of the workout
     */
    void delete(int workoutId);
}
