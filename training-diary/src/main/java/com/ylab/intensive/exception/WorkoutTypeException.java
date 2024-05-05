package com.ylab.intensive.exception;

/**
 * Custom exception class for DAO layer.
 */
public class WorkoutTypeException extends CustomExceptionForBadRequest {
    public WorkoutTypeException(String message) {
        super(message);
    }
}
