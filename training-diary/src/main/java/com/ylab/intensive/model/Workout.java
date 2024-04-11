package com.ylab.intensive.model;

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
    int id; // The ID of the workout
    LocalDate date; // The date of the workout
    Set<String> type; // The type(s) of the workout
    Duration duration; // The duration of the workout
    Float calorie; // The calorie burned during the workout
    Map<String, String> info; // Additional information about the workout
}
