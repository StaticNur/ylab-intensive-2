package com.ylab.intensive.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenDto(
        @NotNull(message = "refreshToken обязательное поля")
        @NotBlank(message = "refreshToken не должен быть пустым")
        String refreshToken) {
}
