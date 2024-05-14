package com.ylab.intensive.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ylab.intensive.util.validation.DurationDeserializer;
import com.ylab.intensive.util.validation.annotation.NullablePattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import java.time.Duration;
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
    @NullablePattern(regexp = "^(?!\\d+$).+", message = "Не должен содержать одни цифры!")
    private String type;

    /**
     * The duration of the workout.
     */
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

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
