package com.ylab.intensive.e2e.config;

import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.service.security.impl.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class HeaderUtils {

    private final JwtTokenService jwtTokenService;
    private final JwtUserDetailsService jwtUserDetailsService;

    public HttpHeaders withAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("admin@example.com");
        headers.add("Authorization", "Bearer " + jwtTokenService.createAccessToken(userDetails));
        return headers;
    }

    public HttpHeaders withUserToken() {
        HttpHeaders headers = new HttpHeaders();
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("user@example.com");
        headers.add("Authorization", "Bearer " + jwtTokenService.createAccessToken(userDetails));
        return headers;
    }
}
