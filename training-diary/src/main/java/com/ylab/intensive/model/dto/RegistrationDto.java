package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.util.validation.annotation.ValidRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Data transfer object (DTO) representing user registration information.
 * <p>
 * This class encapsulates the email address, password, and role for user registration.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    /**
     * The email address of the user.
     */
    @Pattern(regexp = "^(?!\\d+$).+", message = "Не должен содержать одни цифры!")
    @NotNull(message = "Обязательное поля!")
    @NotBlank(message = "Не должен быть пустым!")
    private String email;

    /**
     * The password of the user.
     */
    @NotNull(message = "Обязательное поля!")
    @NotBlank(message = "Не должен быть пустым!")
    private String password;

    /**
     * The role of the user (e.g., ADMIN, USER).
     */
    @ValidRole(allowedRoles = {Role.ADMIN, Role.USER})
    @NotNull(message = "Обязательное поля!")
    private Role role;
}

