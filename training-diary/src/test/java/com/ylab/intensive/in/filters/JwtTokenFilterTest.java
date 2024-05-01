package com.ylab.intensive.in.filters;

import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.util.Converter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private Converter converter;

    @Mock
    private ServletContext servletContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        lenient().when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @Test
    void testDoFilter_PublicPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/training-diary/auth/login");

        jwtTokenFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilter_ValidToken() throws IOException, ServletException {
        String token = "validToken";
        Authentication authentication = new Authentication("user@example.com", Role.ADMIN, true);

        when(request.getRequestURI()).thenReturn("/training-diary/other");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenService.validateToken(token)).thenReturn(true);
        when(jwtTokenService.authentication(token)).thenReturn(authentication);

        jwtTokenFilter.doFilter(request, response, filterChain);

        verify(servletContext).setAttribute("authentication", authentication);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilter_InvalidToken() throws IOException, ServletException {
        String token = "invalidToken";

        when(request.getRequestURI()).thenReturn("/training-diary/other");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenService.validateToken(token)).thenReturn(false);
        when(converter.convertObjectToJson(any(ExceptionResponse.class)))
                .thenReturn("{\"message\":\"Authentication was denied for this request.\"}");

        jwtTokenFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("{\"message\":\"Authentication was denied for this request.\"}", stringWriter.toString());
    }
}
