package com.ylab.intensive.model.entity;

import lombok.AllArgsConstructor;
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
public class Workout {
    /**
     * The ID of the workout
     */
    int id;
    /**
     * The date of the workout
     */
    LocalDate date;
    /**
     * The type(s) of the workout
     */
    Set<String> type;
    /**
     * The duration of the workout
     */
    Duration duration;
    /**
     * The calorie burned during the workout
     */
    Float calorie;
    /**
     * Additional information about the workout
     */
    Map<String, String> info;
}
