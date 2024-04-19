package com.ylab.intensive.service;

import java.util.Map;

public interface WorkoutInfoService {
    void saveWorkoutInfo(int workoutId, String title, String info);

    void updateWorkoutInfo(int workoutId, String title, String info);

    Map<String, String> getInfoByWorkoutId(int workoutId);

    void delete(int workoutId);
}
