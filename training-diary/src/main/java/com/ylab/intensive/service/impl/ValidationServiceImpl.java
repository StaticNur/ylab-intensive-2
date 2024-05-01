package com.ylab.intensive.service.impl;

import com.ylab.intensive.model.dto.ValidationError;
import com.ylab.intensive.service.ValidationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link ValidationService} interface.
 * <p>
 * This class provides methods to validate objects using the Bean Validation API and return validation errors.
 * </p>
 * <p>
 * This implementation is designed to be used in CDI environments and is annotated with {@code @ApplicationScoped}.
 * </p>
 *
 * @since 1.0
 */
@ApplicationScoped
@NoArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    /**
     * Validator instance for performing object validation
     */
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public List<ValidationError> validateAndReturnErrors(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            return violations.stream()
                    .map(violation -> new ValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
