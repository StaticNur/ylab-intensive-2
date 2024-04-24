package com.ylab.intensive.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * Represents a workout.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workout {
    /**
     * The ID of the workout
     */
    private int id;
    /**
     * The ID of the workout
     */
    private int userId;
    /**
     * The date of the workout
     */
    private LocalDate date;
    /**
     * The type(s) of the workout
     */
    private Set<String> type;
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
    private Map<String, String> info;
}
