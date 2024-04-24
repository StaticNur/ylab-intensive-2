package com.ylab.intensive.exception;

/**
 * Exception thrown when a requested workout is not found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
