package com.ylab.intensive.exception;

/**
 * Exception thrown when there is an issue during the user registration process.
 */
public class RegistrationException extends CustomExceptionForBadRequest {
    public RegistrationException(String message) {
        super(message);
    }
}
