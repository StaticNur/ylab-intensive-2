package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.util.validation.annotation.ValidRole;
import jakarta.validation.constraints.NotNull;

/**
 * Data transfer object (DTO) representing the change of user rights request.
 * <p>
 * This DTO encapsulates the new role to be assigned to a user.
 * </p>
 *
 * @since 1.0
 */
public record ChangeUserRightsDto(
        @NotNull(message = "newRole не должен быть пустым!")
        @ValidRole(allowedRoles = {Role.ADMIN, Role.USER})
        Role newRole) {
}
