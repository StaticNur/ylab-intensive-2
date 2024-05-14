package com.ylab.intensive.mapper;

import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper class to convert User entity to UserDto.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User entity);

    User toEntity(UserDto dto);
}
