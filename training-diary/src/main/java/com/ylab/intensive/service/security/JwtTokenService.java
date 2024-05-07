package com.ylab.intensive.service.security;

import com.ylab.intensive.exception.InvalidTokenException;
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
    /**
     * Creates an access token for the given user details.
     *
     * @param userDetails The user details for whom the token is created.
     * @return The generated access token.
     */
    String createAccessToken(UserDetails userDetails);

    /**
     * Creates a refresh token for the given user details.
     *
     * @param userDetails The user details for whom the token is created.
     * @return The generated refresh token.
     */
    String createRefreshToken(UserDetails userDetails);

    /**
     * Refreshes the user's access and refresh tokens.
     *
     * @param refreshToken The refresh token to use for token refreshing.
     * @return The updated JWT response containing the new access and refresh tokens.
     * @throws AccessDeniedException If the refresh token is invalid or expired.
     */
    JwtResponse refreshUserToken(String refreshToken);

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    boolean validateToken(String token);

    /**
     * Extracts the email from the given JWT token.
     *
     * @param token The JWT token from which to extract the email.
     * @return The extracted email.
     * @throws InvalidTokenException If the token is invalid or does not contain an email claim.
     */
    Role extractRoles(String token);

    /**
     * Extracts the roles from the given JWT token.
     *
     * @param token The JWT token from which to extract the roles.
     * @return The extracted roles.
     * @throws InvalidTokenException If the token is invalid or does not contain a roles claim.
     */
    String extractEmail(String token);
}

