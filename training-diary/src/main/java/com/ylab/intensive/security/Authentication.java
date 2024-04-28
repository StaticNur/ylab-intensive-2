package com.ylab.intensive.security;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    private String login;
    private Role role;
    private boolean isAuth;
}
