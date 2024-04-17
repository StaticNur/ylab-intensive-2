package com.ylab.intensive.exception;

/**
 * Exception thrown when there is an issue related to workout information.
 */
public class WorkoutInfoException extends RuntimeException {
    public WorkoutInfoException(String message) {
        super(message);
    }
}
