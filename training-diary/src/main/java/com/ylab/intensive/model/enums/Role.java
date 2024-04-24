package com.ylab.intensive.model.enums;

import lombok.Getter;

/**
 * An enumeration representing user roles.
 */
@Getter
public enum Role {
    ADMIN(1),
    USER(2);

    /**
     * The integer value associated with the Role
     * -- GETTER --
     *  Returns the value corresponding to the given role.
     */
    private final int value;

    /**
     * Constructs an Role with the specified integer value.
     *
     * @param value The integer value of the endpoint
     */
    Role(int value) {
        this.value = value;
    }

    /**
     * Returns the role corresponding to the given value.
     *
     * @param value The value of the role
     * @return The role corresponding to the given value
     * @throws IllegalArgumentException if the provided value is invalid
     */
    public static Role fromValue(String value) {
        try {
            for (Role option : Role.values()) {
                if (option.value == Integer.parseInt(value)) {
                    return option;
                }
            }
            throw new Exception("Invalid Role value: " + value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Role value: " + value);
        }
    }

}
