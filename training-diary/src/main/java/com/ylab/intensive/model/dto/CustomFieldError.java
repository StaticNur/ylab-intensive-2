package com.ylab.intensive.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CustomFieldError {

    @JsonProperty("defaultMessage")
    private String defaultMessage;

    @JsonProperty("field")
    private String field;
}

