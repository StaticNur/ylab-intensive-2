package com.ylab.intensive.exception;

/**
 * Exception thrown when there is an issue related to workouts.
 */
public class WorkoutException extends RuntimeException {
    public WorkoutException(String message) {
        super(message);
    }
}
