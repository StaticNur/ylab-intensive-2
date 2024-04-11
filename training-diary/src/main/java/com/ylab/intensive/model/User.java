package com.ylab.intensive.model;

import com.ylab.intensive.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a user in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id; // The ID of the user
    private String email; // The email address of the user
    private String password; // The password of the user
    private List<Workout> workout; // The list of workouts associated with the user
    private List<String> action; // The list of actions performed by the user (Audit)
    private Role role; // The role of the user (e.g., ADMIN, USER)
}