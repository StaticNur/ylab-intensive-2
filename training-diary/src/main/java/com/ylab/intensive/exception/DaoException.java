package com.ylab.intensive.exception;

/**
 * Custom exception class for DAO layer.
 */
public class DaoException extends RuntimeException {
    public DaoException(String message) {
        super(message);
    }
}

