package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Data transfer object (DTO) representing a user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID uuid;
    /**
     * The email address of the user
     */
    private String email;
    /**
     * The role of the user (e.g., ADMIN, USER)
     */
    private Role role;
    /**
     * The list of workouts associated with the user
     */
    private List<WorkoutDto> workouts;
}
