package com.ylab.intensive.exception;

/**
 * Exception thrown when there is an issue during the user registration process.
 */
public class RegisterException extends RuntimeException {
    public RegisterException(String message) {
        super(message);
    }
}
