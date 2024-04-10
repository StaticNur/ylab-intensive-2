package com.ylab.intensive.service;

import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.exception.DateFormatException;
import com.ylab.intensive.exception.WorkoutException;
import com.ylab.intensive.model.Workout;

import java.util.List;
import java.util.Optional;

public interface WorkoutService {
    void setAuthorizedWorkoutDB(List<Workout> workouts);

    void addTrainingType(String date, String typeName);

    void addWorkout(String date, String typeName, String duration, String calorie) throws WorkoutException, DateFormatException;

    void addWorkoutInfo(String date, String title, String info);

    List<WorkoutDto> getAllWorkouts();

    Optional<WorkoutDto> getWorkoutByDate(String date);

    void updateType(WorkoutDto workoutDto, String oldType, String newType);

    void updateDuration(WorkoutDto workoutDto, String duration);

    void updateCalories(WorkoutDto workoutDto, String calorie);

    void updateAdditionalInfo(WorkoutDto workoutDto, String title, String info);

    void deleteWorkout(String date);

    int getWorkoutStatistics(String begin, String end);
}
