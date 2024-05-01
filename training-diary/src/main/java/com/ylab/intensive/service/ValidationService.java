package com.ylab.intensive.service;

import com.ylab.intensive.model.dto.ValidationError;

import java.util.List;

/**
 * Service interface for validating objects and returning validation errors.
 * <p>
 * This interface defines a method for validating objects and returning a list of validation errors.
 * </p>
 *
 * @since 1.0
 */
public interface ValidationService {

    /**
     * Validates the given object and returns a list of validation errors, if any.
     *
     * @param object The object to validate.
     * @return A list of validation errors, or an empty list if the object is valid.
     */
    List<ValidationError> validateAndReturnErrors(Object object);
}

