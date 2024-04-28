package com.ylab.intensive.service.impl;

import com.ylab.intensive.mapper.ValidationErrorMapper;
import com.ylab.intensive.model.dto.ValidationError;
import com.ylab.intensive.service.ValidationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@NoArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private ValidationErrorMapper mapper;
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Inject
    public ValidationServiceImpl(ValidationErrorMapper mapper) {
        this.mapper = mapper;
    }

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
