package com.ylab.intensive.service;

import com.ylab.intensive.model.entity.Workout;

import java.util.Set;

public interface WorkoutTypeService {
    void saveType(int workoutId, String typeName);

    Set<String> findByWorkoutId(int workoutId);

    void updateType(int workoutId, String oldType, String newType);

    void delete(int workoutId);
}
