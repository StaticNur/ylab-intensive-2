package com.ylab.intensive.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) representing workout type information.
 * <p>
 * This class encapsulates the type of a workout.
 * </p>
 *
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutTypeDto {
    /**
     * The type of the workout.
     */
    @Schema(example = "running")
    @NotBlank(message = "Не должен быть пустым!")
    @NotNull(message = "Обязательное поля!")
    @Pattern(regexp = "^(?!\\d+$).+", message = "Не должен содержать одни цифры или быть пустым!")
    private String type;
}
