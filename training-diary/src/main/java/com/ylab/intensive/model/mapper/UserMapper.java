package com.ylab.intensive.model.mapper;

import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.entity.User;

public class UserMapper {

    public UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }

}
