package com.ylab.intensive.mapper;

import com.ylab.intensive.model.dto.WorkoutInfoDto;
import com.ylab.intensive.model.entity.WorkoutInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper class to convert WorkoutInfo entity to WorkoutInfoDto.
 */
@Mapper
public interface WorkoutInfoMapper {

    WorkoutInfoMapper INSTANCE = Mappers.getMapper(WorkoutInfoMapper.class);

    WorkoutInfoDto toDto(WorkoutInfo entity);

    WorkoutInfo toEntity(WorkoutInfoDto dto);
}
