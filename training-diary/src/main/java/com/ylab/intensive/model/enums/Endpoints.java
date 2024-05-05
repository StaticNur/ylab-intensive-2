package com.ylab.intensive.model.enums;

import lombok.Getter;

/**
 * Enumeration of endpoints representing different actions in the application menu.
 */
@Getter
public enum Endpoints {
    AUDIT("/training-diary/user/audit?page={page}&count={count}", "GET"),

    REGISTRATION("/training-diary/auth/registration", "POST"),
    LOGIN("/training-diary/auth/email", "POST"),

    VIEW_STATISTICS("/training-diary/statistics", "GET"),

    VIEW_TRAININGS_FOR_ALL_USERS("/training-diary/users/workouts", "GET"),
    CHANGE_USER_RIGHTS("/training-diary/users/{uuid}/access", "PATCH"),

    WORKOUTS("/training-diary/workouts", "POST,GET"),
    WORKOUT("/training-diary/workouts/{uuid}", "PUT,DELETE"),

    ADD_ADDITIONAL_INFORMATION("/training-diary/workout-info/{uuid}", "POST"),

    TYPE_WORKOUT("/training-diary/workouts/type", "POST,GET");

    private final String path;
    private final String methods;

    Endpoints(String path, String methods) {
        this.path = path;
        this.methods = methods;
    }
}
