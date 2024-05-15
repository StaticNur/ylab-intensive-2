package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.auditspringbootstarter.annotation.Auditable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing workout type-related operations.
 */
@RestController
@RequestMapping("/training-diary/workouts/type")
@Tag(name = "WorkoutTypeController", description = "Workout Type Controller")
@RequiredArgsConstructor
public class WorkoutTypeController {
    /**
     * Service for workout-related operations
     */
    private final WorkoutService workoutService;

    /**
     * Utility for generating custom error response messages
     */
    private final GeneratorResponseMessage generatorResponseMessage;

    /**
     * Mapper for converting WorkoutType entities to DTOs
     */
    private final WorkoutTypeMapper workoutTypeMapper;

    /**
     * Retrieves workout types for the authenticated user.
     *
     * @return ResponseEntity containing a list of WorkoutTypeDto objects representing the user's workout types
     */
    @GetMapping
    @Operation(summary = "View user workout types", description = "Endpoint to view user workout types.")
    @Auditable(action = "Пользователь просмотрел свои типы тренировок.")
    public ResponseEntity<List<WorkoutTypeDto>> viewTypes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<WorkoutType> workoutTypes = workoutService.getAllType(authentication.getName());
        return ResponseEntity.ok(workoutTypes.stream()
                .map(workoutTypeMapper::toDto)
                .toList());
    }

    /**
     * Saves a new workout type for the authenticated user.
     *
     * @param workoutTypeDto DTO containing details of the workout type to be saved
     * @param bindingResult  BindingResult for validating the request body
     * @return ResponseEntity containing the saved WorkoutTypeDto object
     */
    @PostMapping
    @Operation(summary = "Save user workout type", description = "Endpoint to save user workout type.")
    @Auditable(action = "Пользователь добавил новый тип тренировки.")
    public ResponseEntity<?> saveType(@RequestBody @Valid WorkoutTypeDto workoutTypeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WorkoutType workoutType = workoutService.saveWorkoutType(authentication.getName(), workoutTypeDto.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutTypeMapper.toDto(workoutType));
    }
}
