package com.ylab.intensive.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ylab.intensive.util.validation.DurationDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
