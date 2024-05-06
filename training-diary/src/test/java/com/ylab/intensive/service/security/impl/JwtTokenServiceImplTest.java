package com.ylab.intensive.service.security.impl;

import com.ylab.intensive.exception.AccessDeniedException;
import com.ylab.intensive.exception.InvalidTokenException;
import com.ylab.intensive.model.JwtProperties;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenServiceImpl Tests")
class JwtTokenServiceImplTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtTokenServiceImpl jwtTokenService;

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn("voiw3xyrn0734tcgih3dm9qxwpu8fcnoq37g674fqoxzo83");
        jwtTokenService.init();
    }

    @Test
    @DisplayName("Test creating access token")
    void testCreateAccessToken() {
        User user = new User("testuser", "password", Collections.emptyList());
        when(jwtProperties.getAccess()).thenReturn(1000L);

        String token = jwtTokenService.createAccessToken(user);

        assertNotNull(token);
    }

    @Test
    @DisplayName("Test creating refresh token - success")
    void testCreateRefreshToken() {
        User user = new User("testuser", "password", Collections.emptyList());
        when(jwtProperties.getRefresh()).thenReturn(1000L);

        String token = jwtTokenService.createRefreshToken(user);

        assertNotNull(token);
    }

    @Test
    @DisplayName("Test validating token with invalid token")
    void testValidateTokenWithInvalidToken() {
        String token = "invalid_token";

        assertFalse(jwtTokenService.validateToken(token));
    }

    @Test
    @DisplayName("Test extracting email from token")
    void testExtractEmail() {
        User user = new User("testuser", "password", Collections.emptyList());
        when(jwtProperties.getAccess()).thenReturn(1000L);
        String token = jwtTokenService.createAccessToken(user);

        String email = jwtTokenService.extractEmail(token);

        assertThat(email).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("Test extracting roles from token")
    void testExtractRoles() {
        User user = new User("testuser", "password", Collections.emptyList());
        when(jwtProperties.getAccess()).thenReturn(1000L);
        String token = jwtTokenService.createAccessToken(user);

        assertThatThrownBy(() -> jwtTokenService.extractRoles(token))
                .isInstanceOf(InvalidTokenException.class);
    }
}
