package com.ylab.intensive.dao;

import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.Workout;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutDao {
    void init(List<Workout> workouts);
    int getSize();

    Optional<Workout> findByDate(LocalDate date);

    void saveType(Workout workout, String type);

    void saveWorkout(Workout workout);

    void saveWorkoutInfo(Workout workout, String title, String key);

    List<Workout> findAll();

    void deleteWorkout(LocalDate date);

    void updateWorkoutInfo(WorkoutDto workoutDto, String title, String info);

    void updateCalorie(WorkoutDto workoutDto, Float calorie);

    void updateDuration(WorkoutDto workoutDto, Duration duration);

    void updateType(WorkoutDto workoutDto, String oldType, String newType);

    List<Workout> findByDuration(LocalDate begin, LocalDate end);
}
