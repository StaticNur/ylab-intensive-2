package com.ylab.intensive.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ylab.intensive.util.DurationDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditWorkout {
    private String type;
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration duration;
    private Float calorie;
    private Map<String, String> workoutInfo;
}
