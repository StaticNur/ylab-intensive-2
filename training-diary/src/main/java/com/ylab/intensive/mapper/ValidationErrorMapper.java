package com.ylab.intensive.mapper;

import com.ylab.intensive.model.dto.ValidationError;
import jakarta.validation.ConstraintViolation;
import org.mapstruct.Mapper;

@Mapper
public interface ValidationErrorMapper {
    ValidationError toValidationError(ConstraintViolation<?> violation);

    default String getMessage(ConstraintViolation<?> violation) {
        return violation.getMessage();
    }

    default String getField(ConstraintViolation<?> violation) {
        return violation.getPropertyPath().toString();
    }
}
