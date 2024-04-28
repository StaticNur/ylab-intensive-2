package com.ylab.intensive.util;

import lombok.Data;

@Data
public class JwtProperties {
    private String secret;
    private Long access;
    private Long refresh;
}
