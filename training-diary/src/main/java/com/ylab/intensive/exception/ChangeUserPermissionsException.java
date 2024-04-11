package com.ylab.intensive.exception;

/**
 * Exception thrown when an operation to change user permissions fails.
 */
public class ChangeUserPermissionsException extends RuntimeException {
    public ChangeUserPermissionsException(String message) {
        super(message);
    }
}

