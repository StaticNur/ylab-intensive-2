package com.ylab.intensive.in.security;

import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service interface for JWT token management.
 * <p>
 * This interface defines methods for creating, refreshing, and validating JWT tokens,
 * as well as extracting user information from tokens.
 * </p>
 *
 * @since 1.0
 */
public interface JwtTokenService {

    String createAccessToken(UserDetails userDetails);


    String createRefreshToken(UserDetails userDetail);

    /**
     * Refreshes the user tokens using the provided refresh token.
     *
     * @param refreshToken The refresh token.
     * @return The refreshed JWT response containing new access and refresh tokens.
     */
    JwtResponse refreshUserTokens(String refreshToken);

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts the roles from the provided JWT token.
     *
     * @param token The JWT token.
     * @return The role extracted from the token.
     */
    Role extractRoles(String token);

    /**
     * Extracts the email address from the provided JWT token.
     *
     * @param token The JWT token.
     * @return The email address extracted from the token.
     */
    String extractEmail(String token);
}

