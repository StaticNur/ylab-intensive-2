package com.ylab.intensive.model;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents authentication information for a user.
 * <p>
 * This class contains the user's login, role, and authentication status.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    /**
     * The login or email address of the user.
     */
    private String login;

    /**
     * The role of the user (e.g., ADMIN, USER).
     */
    private Role role;

    /**
     * Indicates whether the user is authenticated.
     */
    private boolean isAuth;
}

