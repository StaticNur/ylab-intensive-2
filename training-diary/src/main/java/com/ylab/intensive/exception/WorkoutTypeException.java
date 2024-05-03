package com.ylab.intensive.exception;

/**
 * Custom exception class for DAO layer.
 */
public class WorkoutTypeException extends RuntimeException {
    public WorkoutTypeException(String message) {
        super(message);
    }
}
