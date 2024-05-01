package com.ylab.intensive.model;

import lombok.Data;

/**
 * Represents properties used for JWT token generation and validation.
 * <p>
 * This class contains the secret key used for token signing, as well as the expiration
 * times for access and refresh tokens.
 * </p>
 *
 * @since 1.0
 */
@Data
public class JwtProperties {
    /**
     * The secret key used for signing JWT tokens.
     */
    private String secret;

    /**
     * The expiration time (in milliseconds) for access tokens.
     */
    private Long access;

    /**
     * The expiration time (in milliseconds) for refresh tokens.
     */
    private Long refresh;
}
