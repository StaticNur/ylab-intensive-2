package com.ylab.intensive.security;

import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.enums.Role;

public interface JwtTokenService {

    String createAccessToken(String login, Role role);

    String createRefreshToken(String login, Role role);

    JwtResponse refreshUserTokens(String refreshToken);

    Authentication authentication(String token);

    boolean validateToken(String token);

    Role extractRoles(String token);

    String extractEmail(String token);
}
