package com.ylab.intensive.service;

import com.ylab.intensive.model.entity.WorkoutType;

import java.util.List;

/**
 * Service interface for workout type operations.
 */
public interface WorkoutTypeService {

    /**
     * Saves a workout type.
     *
     * @param userId   the ID of the workout
     * @param typeName the name of the workout type to save
     */
    WorkoutType saveType(int userId, String typeName);

    /**
     * Finds workout types by workout ID.
     *
     * @return a set of workout types
     */
    List<WorkoutType> findByUserId(int userId);

    /**
     * Updates a workout type.
     *
     * @param workoutId the ID of the workout
     * @param oldType   the old workout type
     * @param newType   the new workout type
     */
    void updateType(int workoutId, String oldType, String newType);

    WorkoutType findById(int id);
}
