package com.ylab.intensive.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) representing user login information.
 * <p>
 * This class encapsulates the email address and password for user login.
 * </p>
 *
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    /**
     * The email address of the user.
     */
    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String email;

    /**
     * The password of the user.
     */
    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String password;
}
