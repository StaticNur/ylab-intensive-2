package com.ylab.intensive.model.dto;


public record JwtResponse(String login,
                          String accessToken,
                          String refreshToken) {
}
