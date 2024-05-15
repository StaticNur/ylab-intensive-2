/*
package com.ylab.intensive.in.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.exception.AuthorizeException;
import com.ylab.intensive.exception.InvalidTokenException;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.security.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тест фильтра JWT токена")
class JwtTokenFilterTest {

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private ObjectMapper jacksonMapper;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setup() throws Exception {
        AutoCloseable closeable = MockitoAnnotations.openMocks(this);
        closeable.close();
    }

    @Test
    @DisplayName("Должен успешно установить аутентификацию при наличии действительного JWT токена")
    void shouldSetAuthenticationWithValidJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String email = "test@example.com";
        String validToken = "validToken";
        request.addHeader("Authorization", "Bearer " + validToken);

        when(jwtTokenService.extractEmail(validToken)).thenReturn(email);
        when(jwtTokenService.extractRoles(validToken)).thenReturn(Role.USER);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class));
    }

    @Test
    @DisplayName("Должен отправить ответ с ошибкой при истекшем сроке действия JWT токена")
    void shouldRespondWithErrorOnExpiredJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String expiredToken = "expiredToken";
        request.addHeader("Authorization", "Bearer " + expiredToken);

        when(jwtTokenService.extractEmail(expiredToken)).thenThrow(ExpiredJwtException.class);
        when(jacksonMapper.writeValueAsString(any())).thenReturn("response");

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class));
        verify(jacksonMapper).writeValueAsString(any(ExceptionResponse.class));
    }

    @Test
    @DisplayName("Должен отправить ответ с ошибкой при недействительном JWT токене")
    void shouldRespondWithErrorOnInvalidJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String invalidToken = "invalidToken";

        request.addHeader("Authorization", "Bearer " + invalidToken);

        when(jwtTokenService.extractEmail(invalidToken)).thenThrow(InvalidTokenException.class);
        when(jacksonMapper.writeValueAsString(any())).thenReturn("response");

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class));
        verify(jacksonMapper).writeValueAsString(any(ExceptionResponse.class));
    }

    @Test
    @DisplayName("Должен отправить ответ с ошибкой при отсутствии авторизации")
    void shouldRespondWithErrorOnUnauthorizedAccess() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String invalidToken = "invalidToken";
        request.addHeader("Authorization", "Bearer " + invalidToken);

        when(jwtTokenService.extractEmail(invalidToken)).thenThrow(AuthorizeException.class);
        when(jacksonMapper.writeValueAsString(any())).thenReturn("response");

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(401);
        verify(filterChain, never()).doFilter(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class));
        verify(jacksonMapper).writeValueAsString(any(ExceptionResponse.class));
    }

    @Test
    @DisplayName("Должен пропустить фильтр, если токен отсутствует в заголовке")
    void shouldPassThroughFilterWithoutToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class));
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}*/
