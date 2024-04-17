package com.ylab.intensive.exception;

/**
 * Exception thrown when there is an issue related to training types.
 */
public class TrainingTypeException extends RuntimeException {
    public TrainingTypeException(String message) {
        super(message);
    }
}
