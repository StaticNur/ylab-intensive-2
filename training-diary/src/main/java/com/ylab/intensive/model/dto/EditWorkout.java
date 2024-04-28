package com.ylab.intensive.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ylab.intensive.util.DurationDeserializer;
import com.ylab.intensive.util.validation.annotation.NullablePattern;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditWorkout {

    @NullablePattern(regexp = "^(?!\\d+$).+", message = "Не должен содержать одни цифры!")
    private String type;

    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;

    @DecimalMin(value = "0.0", inclusive = false, message = "Число не должно быть отрицательным")
    private Float calorie;

    @Nullable
    private Map<String, String> workoutInfo;
}
