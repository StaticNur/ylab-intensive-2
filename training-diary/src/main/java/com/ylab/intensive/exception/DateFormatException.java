package com.ylab.intensive.exception;

/**
 * Exception thrown when there is an issue with date format.
 */
public class DateFormatException extends RuntimeException {
    public DateFormatException(String message) {
        super(message);
    }
}
