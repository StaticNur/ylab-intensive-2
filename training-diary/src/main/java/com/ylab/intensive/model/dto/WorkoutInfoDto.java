package com.ylab.intensive.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Data transfer object (DTO) representing additional workout information.
 * <p>
 * This class encapsulates additional information related to a workout, represented as a map of key-value pairs.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutInfoDto {
    /**
     * The additional workout information represented as a map of key-value pairs.
     */
    @NotNull(message = "Обязательное поля!")
    private Map<String, String> workoutInfo;
}


