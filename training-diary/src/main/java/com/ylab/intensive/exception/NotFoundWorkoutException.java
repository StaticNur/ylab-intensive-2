package com.ylab.intensive.exception;

/**
 * Exception thrown when a requested workout is not found.
 */
public class NotFoundWorkoutException extends RuntimeException {
    public NotFoundWorkoutException(String message) {
        super(message);
    }
}
