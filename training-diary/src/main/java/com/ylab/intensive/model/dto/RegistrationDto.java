package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
