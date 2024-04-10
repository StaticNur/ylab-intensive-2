package com.ylab.intensive.service;

import com.ylab.intensive.model.Workout;

public interface WorkoutService {
    void createWorkout(String username, Workout workout);
    void addWorkoutInfo(String username, Workout workout, String info);
    void showWorkoutHistory(String username);
    void editWorkout(String username, Workout workout);
    void deleteWorkout(String username, Workout workout);
}
