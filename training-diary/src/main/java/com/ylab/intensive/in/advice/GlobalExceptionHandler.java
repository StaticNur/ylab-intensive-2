package com.ylab.intensive.in.advice;

import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice class to handle global exceptions and return appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(AuthorizeException.class)
    ResponseEntity<ExceptionResponse> handleAccessDeniedException(AuthorizeException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler({
            InvalidInputException.class,
            InvalidTokenException.class,
            RegistrationException.class,
            WorkoutException.class,
            WorkoutTypeException.class
    })
    ResponseEntity<ExceptionResponse> handleAccessDeniedException(CustomExceptionForBadRequest exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ExceptionResponse> handleAccessDeniedException(NotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponse> handleAccessDeniedException(Exception exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ExceptionResponse(message));
    }
}
