package com.ylab.intensive.exception;

public class CustomExceptionForBadRequest extends RuntimeException {
    public CustomExceptionForBadRequest(String message) {
        super(message);
    }
}

