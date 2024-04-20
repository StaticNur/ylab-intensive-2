package com.ylab.intensive.model.mapper;

import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.entity.Workout;

/**
 * Mapper class to convert Workout entity to WorkoutDto.
 */
public class WorkoutMapper {

    /**
     * Converts a Workout entity to a WorkoutDto.
     *
     * @param workout the Workout entity to convert
     * @return the converted WorkoutDto
     */
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
