package com.ylab.intensive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) representing workout statistics information.
 * <p>
 * This class encapsulates the sum of calories burned during workouts.
 * </p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    /**
     * The sum of calories burned during workouts.
     */
    private float sumCalorie;
}

