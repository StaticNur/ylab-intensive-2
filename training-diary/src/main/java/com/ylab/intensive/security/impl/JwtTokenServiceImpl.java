package com.ylab.intensive.security.impl;

import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.exception.AccessDeniedException;
import com.ylab.intensive.exception.InvalidTokenException;
import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.security.Authentication;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

public class JwtTokenServiceImpl implements JwtTokenService {
    private final JwtProperties properties;
    private final Key key;

    public JwtTokenServiceImpl(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes());
    }

    @Timed
    @Loggable
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

    @Timed
    @Loggable
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

    @Timed
    @Loggable
    @Override
    public JwtResponse refreshUserTokens(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied!");
        }

        String login = extractEmail(refreshToken);
        Role role = extractRoles(refreshToken);

        return new JwtResponse(login, createAccessToken(login, role), createRefreshToken(login, role));
    }

    @Timed
    @Loggable
    @Override
    public Authentication authentication(String token) {
        if (!validateToken(token)) {
            throw new AccessDeniedException("Access denied!");
        }
        String email = extractEmail(token);
        Role role = extractRoles(token);

        return new Authentication(email, role, true);
    }

    @Timed
    @Loggable
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

    @Timed
    @Loggable
    @Override
    public String extractEmail(String token) {
        Optional<Claims> claims = extractAllClaims(token);
        if (claims.isEmpty() || claims.get().getSubject() == null) {
            throw new InvalidTokenException("Invalid token.");
        }
        return claims.get().getSubject();
    }

    @Timed
    @Loggable
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
