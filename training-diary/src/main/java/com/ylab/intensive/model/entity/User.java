package com.ylab.intensive.model.entity;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * The ID of the user
     */
    private int id;

    private UUID uuid;
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
    private List<Workout> workouts;
    /**
     * The list of actions performed by the user (Audit)
     */
    private List<Audit> action;
    /**
     * The role of the user (e.g., ADMIN, USER)
     */
    private Role role;
}