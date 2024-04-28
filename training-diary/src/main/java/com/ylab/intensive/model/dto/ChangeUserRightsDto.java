package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.util.validation.annotation.ValidRole;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRightsDto(
        @NotNull(message = "newRole не должен быть пустым!")
        @ValidRole(allowedRoles = {Role.ADMIN, Role.USER})
        Role newRole) {
}
