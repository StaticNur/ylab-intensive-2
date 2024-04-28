package com.ylab.intensive.mapper;

import com.ylab.intensive.model.dto.WorkoutTypeDto;
import com.ylab.intensive.model.entity.WorkoutType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper class to convert Workout entity to WorkoutDto.
 */
@Mapper
public interface WorkoutTypeMapper {

    WorkoutTypeMapper INSTANCE = Mappers.getMapper(WorkoutTypeMapper.class);

    WorkoutTypeDto toDto(WorkoutType entity);

    WorkoutType toEntity(WorkoutTypeDto dto);
}
