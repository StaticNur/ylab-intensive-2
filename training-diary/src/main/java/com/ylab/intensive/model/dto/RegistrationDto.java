package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import lombok.Data;

@Data
public class RegistrationDto {
    /**
     * The email address of the user
     */
    private String email;

    private String password;
    /**
     * The role of the user (e.g., ADMIN, USER)
     */
    private Role role;
}
