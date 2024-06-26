package com.ylab.intensive.model.dto;

import com.ylab.intensive.util.converter.Converter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

/**
 * Data transfer object (DTO) representing a workout.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDto {
    /**
     * The UUID of the workout
     */
    @Schema(hidden = true)
    private UUID uuid;

    /**
     * The date of the workout
     */
    @Schema(example = "2024-05-09")
    @NotNull(message = "Обязательное поля!")
    @NotBlank(message = "Не должен быть пустым!")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "Incorrect date format. Should be yyyy-MM-dd")
    private String date;

    /**
     * The type(s) of the workout
     */
    @Schema(example = "running")
    @NotNull(message = "Обязательное поля!")
    @NotBlank(message = "Не должен быть пустым!")
    private String type;

    /**
     * The duration of the workout
     */
    @Schema(example = "3:25:0")
    @Pattern(regexp = "^([0-9]+):([0-5]?[0-9]):([0-5]?[0-9])$",
            message = "Формат должен быть H:M:S где часы это любое натуральное число, а минуты и секунды - значения от 0 до 59")
    private String duration;

    /**
     * The calorie burned during the workout
     */
    @Schema(example = "1000")
    @NotNull(message = "Значение не может быть пустым")
    @Min(value = 0, message = "Число не должно быть отрицательным")
    private Float calorie;

    /**
     * Additional information about the workout
     */
    private Map<String, String> workoutInfo;

    public void setDuration(String durationStr){
        this.duration = Converter.convertIso8601ToHMS(durationStr);
    }
}
