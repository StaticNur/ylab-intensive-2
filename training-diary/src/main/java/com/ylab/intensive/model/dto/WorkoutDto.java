package com.ylab.intensive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDto {
    LocalDate date;
    Set<String> type;
    Duration duration;
    Float calorie;
    Map<String, String> info;
}
