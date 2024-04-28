package com.ylab.intensive.security;

import lombok.Data;

@Data
public class JwtProperties {
    private String secret;
    private Long access;
    private Long refresh;
}
