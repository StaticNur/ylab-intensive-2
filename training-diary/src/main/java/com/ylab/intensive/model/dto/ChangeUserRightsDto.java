package com.ylab.intensive.model.dto;

import com.ylab.intensive.model.enums.Role;

public record ChangeUserRightsDto(Role newRole) {
}
