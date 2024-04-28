package com.ylab.intensive.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ylab.intensive.util.DurationDeserializer;
import jakarta.validation.constraints.DecimalMin;
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

    private UUID uuid;

    /**
     * The date of the workout
     */
    @NotNull(message = "Обязательное поля!")
    @NotBlank(message = "Не должен быть пустым!")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "Incorrect date format. Should be yyyy-MM-dd")
    private String date;

    /**
     * The type(s) of the workout
     */
    @NotNull(message = "Обязательное поля!")
    @NotBlank(message = "Не должен быть пустым!")
    private String type;

    /**
     * The duration of the workout
     */
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    /**
     * The calorie burned during the workout
     */
    @NotNull(message = "Значение не может быть пустым")
    @DecimalMin(value = "0.0", inclusive = false, message = "Число не должно быть отрицательным")
    private Float calorie;

    /**
     * Additional information about the workout
     */
    private Map<String, String> workoutInfo;
}
