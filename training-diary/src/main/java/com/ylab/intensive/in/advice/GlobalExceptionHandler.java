package com.ylab.intensive.in.advice;

import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice class to handle global exceptions and return appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AccessDeniedException and returns a response with HTTP status 403 (FORBIDDEN).
     *
     * @param exception AccessDeniedException object representing the exception
     * @return ResponseEntity containing an ExceptionResponse object with error details
     */
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    /**
     * Handles AuthorizeException and returns a response with HTTP status 401 (UNAUTHORIZED).
     *
     * @param exception AuthorizeException object representing the exception
     * @return ResponseEntity containing an ExceptionResponse object with error details
     */
    @ExceptionHandler(AuthorizeException.class)
    ResponseEntity<ExceptionResponse> handleAuthorizeExceptionException(AuthorizeException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    /**
     * Handles various custom exceptions for bad requests and returns a response with HTTP status 400 (BAD REQUEST).
     *
     * @param exception CustomExceptionForBadRequest object representing the exception
     * @return ResponseEntity containing an ExceptionResponse object with error details
     */
    @ExceptionHandler({
            InvalidInputException.class,
            InvalidTokenException.class,
            RegistrationException.class,
            WorkoutException.class,
            WorkoutTypeException.class
    })
    ResponseEntity<ExceptionResponse> handleBadRequestException(CustomExceptionForBadRequest exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles NotFoundException and returns a response with HTTP status 404 (NOT FOUND).
     *
     * @param exception NotFoundException object representing the exception
     * @return ResponseEntity containing an ExceptionResponse object with error details
     */
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ExceptionResponse> handleNotFoundExceptionException(NotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Builds a ResponseEntity with the given HTTP status and error message.
     *
     * @param status  HTTP status code for the response
     * @param message Error message to be included in the response
     * @return ResponseEntity containing an ExceptionResponse object with error details
     */
    private ResponseEntity<ExceptionResponse> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ExceptionResponse(message));
    }
}
