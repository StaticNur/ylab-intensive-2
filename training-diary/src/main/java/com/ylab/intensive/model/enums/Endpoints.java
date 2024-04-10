package com.ylab.intensive.model.enums;

import lombok.Getter;

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
    EXIT(12);

    private final int value;

    Endpoints(int value) {
        this.value = value;
    }

    public static Endpoints fromValue(String value) {
        try {
            for (Endpoints option : Endpoints.values()) {
                if (option.value == Integer.parseInt(value)) {
                    return option;
                }
            }
            throw new Exception("Invalid menu option value: " + value);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid menu option value: " + value);
        }
    }
}
