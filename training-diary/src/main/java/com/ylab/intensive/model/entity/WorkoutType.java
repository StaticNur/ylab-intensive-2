package com.ylab.intensive.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a type of workout.
 * <p>
 * This class encapsulates information about a type of workout, including its unique identifier,
 * the user ID associated with the type,
 * and the name of the type.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutType {
    /**
     * The unique identifier of the workout type.
     */
    private int id;

    /**
     * The user ID associated with the workout type.
     */
    private int userId;

    /**
     * The name of the workout type.
     */
    private String type;
}

