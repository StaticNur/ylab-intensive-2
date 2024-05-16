package com.ylab.intensive.model.dto;

import com.ylab.intensive.util.validation.annotation.NullablePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * Represents data for editing workout information.
 * <p>
 * This class encapsulates the details to be edited for a workout, including the type, duration, calorie count, and additional workout information.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditWorkout {
    /**
     * The type of the workout.
     */
    @Schema(example = "running")
    @NullablePattern(regexp = "^(?!\\d+$).+", message = "Не должен содержать одни цифры!")
    private String type;

    /**
     * The duration of the workout.
     */
    @Schema(example = "1:25:0")
    @Pattern(regexp = "^([0-9]+):([0-5]?[0-9]):([0-5]?[0-9])$",
            message = "Формат должен быть H:M:S где часы это любое натуральное число, а минуты и секунды - значения от 0 до 59")
    private String duration;

    /**
     * The calorie count of the workout.
     */
    @Min(value = 0, message = "Число не должно быть отрицательным")
    private Float calorie;

    /**
     * Additional information related to the workout.
     */
    @Nullable
    private Map<String, String> workoutInfo;
}
