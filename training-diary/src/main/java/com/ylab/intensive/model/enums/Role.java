package com.ylab.intensive.model.enums;

/**
 * An enumeration representing user roles.
 */
public enum Role {
    ADMIN(1),
    USER(2);

    private final int value;


    Role(int value) {
        this.value = value;
    }


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

    public int getValue() {
        return this.value;
    }
}
