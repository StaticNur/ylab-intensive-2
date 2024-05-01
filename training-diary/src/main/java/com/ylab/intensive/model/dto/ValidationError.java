package com.ylab.intensive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a validation error.
 * <p>
 * This class encapsulates information about a validation error, including the field name and error message.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    /**
     * The name of the field where the validation error occurred.
     */
    private String field;

    /**
     * The error message describing the validation failure.
     */
    private String message;
}

