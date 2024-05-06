package com.ylab.intensive.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record RefreshTokenDto(
        @NotNull(message = "refreshToken обязательное поля")
        @NotBlank(message = "refreshToken не должен быть пустым")
        String refreshToken) {
}
