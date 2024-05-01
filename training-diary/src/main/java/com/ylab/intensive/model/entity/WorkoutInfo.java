package com.ylab.intensive.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents additional information related to a workout.
 * <p>
 * This class encapsulates additional information related to a workout,
 * including its unique identifier, the workout ID associated with the information,
 * and a map of key-value pairs representing the additional workout information.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutInfo {
    /**
     * The unique identifier of the workout information.
     */
    private int id;

    /**
     * The workout ID associated with the information.
     */
    private int workoutId;

    /**
     * The additional workout information represented as a map of key-value pairs.
     */
    private Map<String, String> workoutInfo;
}

