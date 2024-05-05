package com.ylab.intensive.in.advice;

import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.zip.DataFormatException;


/**
 * Controller advice class to handle global exceptions and return appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            AccessDeniedException.class,
            ChangeUserPermissionsException.class
    })
    ResponseEntity<ExceptionResponse> handleAuthorizeException(RuntimeException exception) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(DaoException.class)
    ResponseEntity<ExceptionResponse> handleDuplicateRecordException(DaoException exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler({
            RegistrationException.class,
            AuthorizeException.class,
            InvalidTokenException.class,
            DataFormatException.class,
            InvalidUUIDException.class,
            TrainingTypeException.class,
            WorkoutException.class,
            WorkoutInfoException.class,
            WorkoutTypeException.class,
            NotFoundException.class,
            InvalidInputException.class,
    })
    ResponseEntity<ExceptionResponse> handleInvalidCredentialsException(RuntimeException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ExceptionResponse> handleUserNotFoundException(RuntimeException exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ExceptionResponse(message));
    }
}
