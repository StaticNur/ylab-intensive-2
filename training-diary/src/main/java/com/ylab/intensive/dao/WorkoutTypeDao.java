package com.ylab.intensive.dao;

import java.util.Set;

public interface WorkoutTypeDao {

    /**
     * Saves the type of a workout.
     *
     * @param workoutId The workout id to save the type for
     * @param type      The type of the workout to save
     */
    void saveType(int workoutId, String type);


    /**
     * Updates the type of a workout.
     *
     * @param workoutId The workout id containing the type to update
     * @param oldType   The old type of the workout
     * @param newType   The new type of the workout
     */
    void updateType(int workoutId, String oldType, String newType);

    Set<String> findByWorkoutId(int workoutId);

    void delete(int workoutId);
}
