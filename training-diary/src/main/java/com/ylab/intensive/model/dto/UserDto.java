package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) representing a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private Role role;
}
