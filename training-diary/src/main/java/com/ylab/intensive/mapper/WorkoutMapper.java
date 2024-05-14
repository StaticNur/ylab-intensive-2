package com.ylab.intensive.mapper;

import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.entity.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper class to convert Workout entity to WorkoutDto.
 */
@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    WorkoutMapper INSTANCE = Mappers.getMapper(WorkoutMapper.class);

    WorkoutDto toDto(Workout entity);

    Workout toEntity(WorkoutDto dto);
}
