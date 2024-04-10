package com.ylab.intensive.exception;

public class NotFoundWorkoutException extends RuntimeException {
    public NotFoundWorkoutException(String message) {
        super(message);
    }
}
