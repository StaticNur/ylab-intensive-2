package com.ylab.intensive.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a workout.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workout {
    /**
     * The ID of the workout
     */
    private int id;

    /**
     * The UUID of the workout
     */
    private UUID uuid;

    /**
     * The userId of the workout
     */
    private int userId;

    /**
     * The type of the workout
     */
    private String type;

    /**
     * The date of the workout
     */
    private LocalDate date;

    /**
     * The duration of the workout
     */
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
