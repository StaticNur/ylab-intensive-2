package com.ylab.intensive.model.enums;

import lombok.Getter;

/**
 * Enumeration of endpoints representing different actions in the application menu.
 */
@Getter
public enum Endpoints {
    REGISTRATION(1),
    LOGIN(2),
    ADD_TYPE_WORKOUT(3),
    ADD_TRAINING(4),
    ADD_ADDITIONAL_INFORMATION(5),
    VIEW_TRAININGS(6),
    EDIT_TRAINING(7),
    DELETE_TRAINING(8),
    CHANGE_USER_RIGHTS(9),
    VIEW_STATISTICS(10),
    AUDIT(11),
    EXIT(12),
    VIEW_All_TRAININGS(13);

    /**
     * The integer value associated with the endpoint
     */
    private final int value;

    /**
     * Constructs an endpoint with the specified integer value.
     *
     * @param value The integer value of the endpoint
     */
    Endpoints(int value) {
        this.value = value;
    }

    /**
     * Returns the endpoint corresponding to the given value.
     *
     * @param value The value of the endpoint
     * @return The endpoint corresponding to the given value
     * @throws IllegalArgumentException if the provided value is invalid
     */
    public static Endpoints fromValue(String value) {
        try {
            for (Endpoints option : Endpoints.values()) {
                if (option.value == Integer.parseInt(value)) {
                    return option;
                }
            }
            throw new Exception("Invalid menu option value: " + value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid menu option value: " + value);
        }
    }
}
