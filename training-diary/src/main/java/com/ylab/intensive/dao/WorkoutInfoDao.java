package com.ylab.intensive.dao;

import com.ylab.intensive.model.entity.Workout;

import java.util.Map;

public interface WorkoutInfoDao {

    /**
     * Saves additional information for a workout.
     *
     * @param workoutId The workout id to save the information for
     * @param title   The title of the information
     * @param info    The information to save
     */

    void saveWorkoutInfo(int workoutId, String title, String info);


    /**
     * Updates the information of a workout.
     *
     * @param workoutId The workout id containing the information to update
     * @param title   The title of the information to update
     * @param info    The new information
     */
    void updateWorkoutInfo(int workoutId, String title, String info);

    Map<String, String> findByWorkoutId(int workoutId);

    void delete(int workoutId);
}
