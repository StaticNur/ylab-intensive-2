package com.ylab.intensive.service;

import com.ylab.intensive.model.dto.ValidationError;

import java.util.List;

public interface ValidationService {
    List<ValidationError> validateAndReturnErrors(Object object);
}
