package com.ylab.intensive.util.validation;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.util.validation.annotation.ValidRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Validator implementation for the {@link ValidRole} annotation.
 * This validator checks if a given role is one of the allowed roles specified in the annotation.
 */
public class RoleValidator implements ConstraintValidator<ValidRole, Role> {
    private Role[] allowedRoles;

    /**
     * Initializes the validator with the allowed roles specified in the annotation.
     *
     * @param constraintAnnotation The {@link ValidRole} annotation instance.
     */
    @Override
    public void initialize(ValidRole constraintAnnotation) {
        allowedRoles = constraintAnnotation.allowedRoles();
    }

    /**
     * Validates if the given role is one of the allowed roles.
     *
     * @param role    The role to validate.
     * @param context The validation context.
     * @return {@code true} if the role is one of the allowed roles, {@code false} otherwise.
     */
    @Override
    public boolean isValid(Role role, ConstraintValidatorContext context) {
        return Arrays.asList(allowedRoles).contains(role);
    }
}

