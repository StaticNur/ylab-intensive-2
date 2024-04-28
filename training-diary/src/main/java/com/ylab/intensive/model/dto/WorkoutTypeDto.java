package com.ylab.intensive.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutTypeDto {

    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    @Pattern(regexp = "^(?!\\d+$).+", message = "Не должен содержать одни цифры или быть пустым!")
    private String type;
}
