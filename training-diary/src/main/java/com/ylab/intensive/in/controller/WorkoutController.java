package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.MetricService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
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
 * Controller class for managing workout-related operations.
 */
@RestController
@RequestMapping("/training-diary")
@Tag(name = "WorkoutController", description = "Workout Controller")
@RequiredArgsConstructor
public class WorkoutController {
    /**
     * Service for workout-related operations
     */
    private final WorkoutService workoutService;

    /**
     * Mapper for converting Workout entities to DTOs
     */
    private final WorkoutMapper workoutMapper;

    /**
     * Utility for generating custom error response messages
     */
    private final GeneratorResponseMessage generatorResponseMessage;

    /**
     * The MetricService instance used for managing and tracking metrics related to greetings.
     */
    private final MetricService metricService;

    /**
     * Retrieves training statistics (number of calories burned over time).
     *
     * @param begin Start date for the statistics (default: "1970-01-01")
     * @param end   End date for the statistics (default: "2030-01-01")
     * @return ResponseEntity containing a StatisticsDto object
     */
    @GetMapping("/statistics")
    @Operation(summary = "Obtaining training statistics (number of calories burned over time)",
            description = "Endpoint to obtain training statistics.")
    @Auditable(action = "Пользователь просмотрел статистики по тренировкам(количество потраченных калорий в разрезе времени)")
    public ResponseEntity<StatisticsDto> viewStatistics(@RequestParam(value = "begin", defaultValue = "1970-01-01") String begin,
                                                        @RequestParam(value = "end", defaultValue = "2030-01-01") String end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StatisticsDto statistics = workoutService.getWorkoutStatistics(authentication.getName(), begin, end);
        metricService.incrementGreetingCount(authentication.getName());
        return ResponseEntity.ok(statistics);
    }

    /**
     * Adds additional information about the workout.
     *
     * @param uuid           UUID of the workout
     * @param workoutInfoDto DTO containing additional workout information
     * @param bindingResult  BindingResult for validating the request body
     * @return ResponseEntity containing a WorkoutDto object
     */
    @PostMapping("/workout-info/{uuid}")
    @Operation(summary = "Add additional information about the workout",
            description = "Endpoint to add additional information about the workout.")
    @Auditable(action = "Пользователь добавил дополнительную информацию о тренировке uuid которого равен @uuid")
    public ResponseEntity<?> saveAdditionalInformation(@PathVariable("uuid") String uuid,
                                                       @RequestBody @Valid WorkoutInfoDto workoutInfoDto,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Workout workout = workoutService.addWorkoutInfo(authentication.getName(), uuid, workoutInfoDto);
        metricService.incrementGreetingCount(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toDto(workout));
    }

    /**
     * Retrieves the previous workouts of the authenticated user.
     *
     * @return ResponseEntity containing a list of WorkoutDto objects representing the user's workouts
     */
    @GetMapping("/workouts")
    @Operation(summary = "View your previous workouts", description = "Endpoint to view previous workouts of the user.")
    @Auditable(action = "Пользователь просмотрел свои предыдущие тренировки.")
    public ResponseEntity<List<WorkoutDto>> viewWorkouts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Workout> workouts = workoutService.getAllWorkoutsByUser(authentication.getName());
        metricService.incrementGreetingCount(authentication.getName());
        return ResponseEntity.ok(workouts.stream().map(workoutMapper::toDto).toList());
    }

    /**
     * Saves a new workout for the authenticated user.
     *
     * @param workoutDto    DTO containing details of the workout to be saved
     * @param bindingResult BindingResult for validating the request body
     * @return ResponseEntity containing the saved WorkoutDto object
     */
    @PostMapping("/workouts")
    @Operation(summary = "Save user workout", description = "Endpoint to save user workout.")
    @Auditable(action = "Пользователь добавил новую тренировку.")
    public ResponseEntity<?> saveWorkout(@RequestBody @Valid WorkoutDto workoutDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WorkoutDto workoutDtoSaved = workoutService.addWorkout(authentication.getName(), workoutDto);
        metricService.incrementGreetingCount(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutDtoSaved);
    }

    /**
     * Edits an existing workout for the authenticated user.
     *
     * @param uuid          UUID of the workout to be edited
     * @param editWorkout   DTO containing details of the edited workout
     * @param bindingResult BindingResult for validating the request body
     * @return ResponseEntity containing the edited WorkoutDto object
     */
    @PutMapping("/workouts/{uuid}")
    @Operation(summary = "Edit user workout", description = "Endpoint to edit user workout.")
    @Auditable(action = "Пользователь редактировал тренировку по uuid=@uuid")
    public ResponseEntity<?> editWorkout(@PathVariable("uuid") String uuid,
                                         @RequestBody @Valid EditWorkout editWorkout, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Workout workout = workoutService.updateWorkout(authentication.getName(), uuid, editWorkout);
        metricService.incrementGreetingCount(authentication.getName());
        return ResponseEntity.ok(workoutMapper.toDto(workout));
    }

    /**
     * Deletes a workout for the authenticated user.
     *
     * @param uuid UUID of the workout to be deleted
     * @return ResponseEntity containing a SuccessResponse object
     */
    @DeleteMapping("/workouts/{uuid}")
    @Operation(summary = "Delete a user's workout", description = "Endpoint to delete a user's workout.")
    @Auditable(action = "Пользователь удалил тренировку по uuid=@uuid")
    public ResponseEntity<SuccessResponse> deleteWorkouts(@PathVariable("uuid") String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        workoutService.deleteWorkout(authentication.getName(), uuid);
        metricService.incrementGreetingCount(authentication.getName());
        return ResponseEntity.ok(new SuccessResponse("Данные успешно удалены!"));
    }
}
