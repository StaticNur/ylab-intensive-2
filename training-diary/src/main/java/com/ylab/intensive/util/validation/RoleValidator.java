package com.ylab.intensive.util.validation;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.util.validation.annotation.ValidRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<ValidRole, Role> {
    private Role[] allowedRoles;

    @Override
    public void initialize(ValidRole constraintAnnotation) {
        allowedRoles = constraintAnnotation.allowedRoles();
    }

    @Override
    public boolean isValid(Role role, ConstraintValidatorContext context) {
        return Arrays.asList(allowedRoles).contains(role);
    }
}
