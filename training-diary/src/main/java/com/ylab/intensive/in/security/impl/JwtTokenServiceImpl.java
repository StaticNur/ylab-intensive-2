package com.ylab.intensive.in.security.impl;

import com.ylab.intensive.exception.AccessDeniedException;
import com.ylab.intensive.exception.InvalidTokenException;
import com.ylab.intensive.in.security.JwtTokenService;
import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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
public class JwtTokenServiceImpl implements JwtTokenService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access}")
    private Long access;
    @Value("${jwt.refresh}")
    private Long refresh;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String createAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", rolesList);

        Date now = new Date();
        Date validity = new Date(now.getTime() + access);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    @Override
    public String createRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", rolesList);

        Date now = new Date();
        Date validity = new Date(now.getTime() + refresh);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    @Override
    public JwtResponse refreshUserTokens(String refreshToken) throws AccessDeniedException {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied!");
        }

        String login = extractEmail(refreshToken);

        return null;//new JwtResponse(login, createAccessToken(login), createRefreshToken(login)); //TODO
    }

    @Override
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().build().parseSignedClaims(token);
        return !claims.getPayload().getExpiration().before(new Date());
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
            Jws<Claims> claimsJws = Jwts.parser()
                                        .verifyWith(key).build()
                                        .parseSignedClaims(token);
            return Optional.ofNullable(claimsJws.getPayload());
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Malformed JWT token." + e.getMessage());
        }
    }
}
