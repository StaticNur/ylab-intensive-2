package com.ylab.intensive.security.impl;

import com.ylab.intensive.exception.AccessDeniedException;
import com.ylab.intensive.exception.InvalidTokenException;
import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.model.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

/**
 * Implementation of the {@link JwtTokenService} interface for managing JWT tokens.
 * <p>
 * This class provides methods for creating access and refresh tokens, refreshing user tokens,
 * validating tokens, and extracting user information from tokens.
 * </p>
 *
 * @since 1.0
 */
public class JwtTokenServiceImpl implements JwtTokenService {
    /**
     * The properties for JWT token configuration.
     */
    private final JwtProperties properties;

    /**
     * The key used for signing JWT tokens.
     */
    private final Key key;

    /**
     * Constructs a new JwtTokenServiceImpl with the specified JWT properties.
     *
     * @param properties the JWT properties for token configuration
     */
    public JwtTokenServiceImpl(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
    }

    @Override
    public String createAccessToken(String login, Role role) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role.name());
        Date now = new Date();
        Date validity = new Date(now.getTime() + properties.getAccess());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    @Override
    public String createRefreshToken(String login, Role role) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role.name());
        Date now = new Date();
        Date validity = new Date(now.getTime() + properties.getRefresh());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    @Override
    public JwtResponse refreshUserTokens(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied!");
        }

        String login = extractEmail(refreshToken);
        Role role = extractRoles(refreshToken);

        return new JwtResponse(login, createAccessToken(login, role), createRefreshToken(login, role));
    }

    @Override
    public Authentication authentication(String token) {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied!");
        }
        String email = extractEmail(token);
        Role role = extractRoles(token);

        return new Authentication(email, role, true);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public String extractEmail(String token) {
        Optional<Claims> claims = extractAllClaims(token);
        if (claims.isEmpty() || claims.get().getSubject() == null) {
            throw new InvalidTokenException("Invalid token.");
        }
        return claims.get().getSubject();
    }

    @Override
    public Role extractRoles(String token) {
        Optional<Claims> claims = extractAllClaims(token);
        if (claims.isEmpty()) {
            throw new InvalidTokenException("Invalid token.");
        }
        String roleStr = claims.get().get("role", String.class);
        try {
            return Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    private Optional<Claims> extractAllClaims(String token) {
        try {
            return Optional.ofNullable(Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody());
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Malformed JWT token." + e.getMessage());
        }
    }
}
