package com.ylab.intensive.exception;

public class InvalidTokenException extends CustomExceptionForBadRequest {
    public InvalidTokenException(String message) {
        super(message);
    }
}
