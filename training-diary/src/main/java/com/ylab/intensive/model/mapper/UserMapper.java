package com.ylab.intensive.model.mapper;

import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.entity.User;

/**
 * Mapper class to convert User entity to UserDto.
 */
public class UserMapper {

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity to convert
     * @return the converted UserDto
     */
    public UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }
}
