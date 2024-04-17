package com.ylab.intensive.model.mapper;

import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.entity.Workout;

public class WorkoutMapper {

    public WorkoutDto entityToDto(Workout workout) {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(workout.getDate());
        workoutDto.setType(workout.getType());
        workoutDto.setDuration(workout.getDuration());
        workoutDto.setCalorie(workout.getCalorie());
        workoutDto.setInfo(workout.getInfo());
        return workoutDto;
    }
}
