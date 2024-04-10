package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.Workout;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkoutDaoImpl implements WorkoutDao {
    private List<Workout> workoutDB;
    public void init(List<Workout> workoutDB){
        this.workoutDB = workoutDB;
    }

    @Override
    public int getSize() {
        return workoutDB.size();
    }

    @Override
    public Optional<Workout> findByDate(LocalDate date) {
        return workoutDB.stream()
                .filter(workout -> workout.getDate().equals(date))
                .findFirst();
    }

    @Override
    public void saveType(Workout workout, String type) {
        workout.getType().add(type);
    }

    @Override
    public void saveWorkout(Workout workout) {
        workoutDB.add(workout);
    }

    @Override
    public void saveWorkoutInfo(Workout workout, String title, String info) {
        workout.getInfo().put(title, info);
    }

    @Override
    public List<Workout> findAll() {
        return workoutDB;
    }

    @Override
    public void deleteWorkout(LocalDate date) {
        workoutDB.removeIf(workout -> workout.getDate().equals(date));
    }

    @Override
    public void updateWorkoutInfo(WorkoutDto workoutDto, String title, String info) {
        Optional<Workout> workout = findByDate(workoutDto.getDate());
        workout.get().getInfo().put(title, info);
    }

    @Override
    public void updateCalorie(WorkoutDto workoutDto, Float calorie) {
        Optional<Workout> workout = findByDate(workoutDto.getDate());
        workout.get().setCalorie(calorie);
    }

    @Override
    public void updateDuration(WorkoutDto workoutDto, Duration duration) {
        Optional<Workout> workout = findByDate(workoutDto.getDate());
        workout.get().setDuration(duration);
    }

    @Override
    public void updateType(WorkoutDto workoutDto, String oldType, String newType) {
        Optional<Workout> workout = findByDate(workoutDto.getDate());
        workout.get().getType().remove(oldType);
        workout.get().getType().add(newType);
    }

    @Override
    public List<Workout> findByDuration(LocalDate begin, LocalDate end) {
        List<Workout> workoutFromTheRange = new ArrayList<>();

        for (Workout workout : workoutDB) {
            LocalDate workoutDate = workout.getDate();

            if (workoutDate.isAfter(begin) && workoutDate.isBefore(end)) {
                workoutFromTheRange.add(workout);
            }
        }
        return workoutFromTheRange;
    }
}
