package com.ylab.intensive.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ylab.intensive.util.DurationDeserializer;
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
    private String date;

    /**
     * The type(s) of the workout
     */
    private String type;

    /**
     * The duration of the workout
     */
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    /**
     * The calorie burned during the workout
     */
    private Float calorie;

    /**
     * Additional information about the workout
     */
    private Map<String, String> workoutInfo;
}
