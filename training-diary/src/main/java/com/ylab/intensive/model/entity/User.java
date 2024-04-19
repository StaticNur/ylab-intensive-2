package com.ylab.intensive.model.entity;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a user in the system.
 */
@Data
@Builder
public class User {
    /**
     * The ID of the user
     */
    private int id;
    /**
     * The email address of the user
     */
    private String email;
    /**
     * The password of the user
     */
    private String password;
    /**
     * The list of workouts associated with the user
     */
    private List<Workout> workout;
    /**
     * The list of actions performed by the user (Audit)
     */
    private List<String> action;
    /**
     * The role of the user (e.g., ADMIN, USER)
     */
    private Role role;
}