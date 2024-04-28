package com.ylab.intensive.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    /**
     * The email address of the user
     */
    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String email;

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    private String password;
}
