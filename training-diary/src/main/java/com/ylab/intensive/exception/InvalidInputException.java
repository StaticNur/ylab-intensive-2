package com.ylab.intensive.exception;

public class InvalidInputException extends CustomExceptionForBadRequest {
    public InvalidInputException(String message) {
        super(message);
    }
}
