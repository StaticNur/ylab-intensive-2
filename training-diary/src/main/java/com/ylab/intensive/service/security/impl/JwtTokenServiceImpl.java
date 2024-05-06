package com.ylab.intensive.service.security.impl;

import com.ylab.intensive.exception.AccessDeniedException;
import com.ylab.intensive.exception.InvalidTokenException;
import com.ylab.intensive.model.JwtProperties;
import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.*;

/**
 * Implementation of the {@link JwtTokenService} interface for managing JWT tokens.
 * <p>
 * This class provides methods for creating access and refresh tokens, refreshing user tokens,
 * validating tokens, and extracting user information from tokens.
 * </p>
 *
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    /**
     * Properties related to JWT configuration.
     */
    private final JwtProperties jwtProperties;

    /**
     * Secret key used for JWT signing and verification.
     */
    private SecretKey key;

    /**
     * Initializes the secret key used for JWT operations.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String createAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", rolesList);

        return generateToken(claims, userDetails.getUsername(), jwtProperties.getAccess());
    }

    @Override
    public String createRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", rolesList);

        return generateToken(claims, userDetails.getUsername(), jwtProperties.getRefresh());
    }

    @Override
    public JwtResponse refreshUserToken(String refreshToken) throws AccessDeniedException {
        try {
            if (validateToken(refreshToken)) {
                String email = extractEmail(refreshToken);
                Role role = extractRoles(refreshToken);
                Map<String, Object> roles = new HashMap<>();
                roles.put("roles", Collections.singletonList(role.name()));

                String accessToken = generateToken(roles, email, jwtProperties.getAccess());
                String newRefreshToken = generateToken(roles, email, jwtProperties.getRefresh());

                return new JwtResponse(email, accessToken, newRefreshToken);
            } else throw new IllegalArgumentException();
        } catch (ExpiredJwtException | UnsupportedJwtException
                 | MalformedJwtException | IllegalArgumentException e) {
            throw new AccessDeniedException("Invalid or expired refresh token");
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key).build().parseSignedClaims(token);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException | UnsupportedJwtException
                 | MalformedJwtException | IllegalArgumentException e) {
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
        List<String> roles = claims.get().get("roles", List.class);
        if (!roles.isEmpty()) {
            try {
                return Role.valueOf(roles.get(0));
            } catch (IllegalArgumentException e) {
                throw new InvalidTokenException(e.getMessage());
            }
        } else {
            throw new InvalidTokenException("Token does not contain roles claim.");
        }
    }

    /**
     * Generates a JWT token based on the provided claims and expiration time.
     *
     * @param claims         The claims to include in the token.
     * @param username       The subject of the token.
     * @param expirationTime The expiration time of the token.
     * @return The generated JWT token.
     */
    private String generateToken(Map<String, Object> claims, String username, long expirationTime) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token The JWT token from which to extract the claims.
     * @return The optional containing all extracted claims.
     */
    private Optional<Claims> extractAllClaims(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token);
            return Optional.ofNullable(claimsJws.getPayload());
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Malformed JWT token." + e.getMessage());
        }
    }
}
