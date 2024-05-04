package com.ylab.intensive.in.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.exception.AuthorizeException;
import com.ylab.intensive.exception.InvalidTokenException;
import com.ylab.intensive.in.security.JwtTokenService;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final ObjectMapper jacksonMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7).trim();

            if (!jwt.isEmpty()) {
                try {
                    email = jwtTokenService.extractEmail(jwt);
                } catch (ExpiredJwtException exception) {
                    sendResponse(response, "The token's lifetime has expired.");
                    return;
                } catch (InvalidTokenException | AuthorizeException e) {
                    sendResponse(response, e.getMessage());
                    return;

                }
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Role role = jwtTokenService.extractRoles(jwt);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, null, Collections.singleton(new SimpleGrantedAuthority(role.name())));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } catch (RuntimeException e) {
                sendResponse(response, e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ExceptionResponse exceptionResponse = new ExceptionResponse(message);
        String jsonResponse = jacksonMapper.writeValueAsString(exceptionResponse);
        response.getWriter().write(jsonResponse);
    }

}

