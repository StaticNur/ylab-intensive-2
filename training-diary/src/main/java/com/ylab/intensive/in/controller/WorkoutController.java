package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for handling authentication operations.
 */
@RestController
@RequestMapping("/training-diary")
@Api(value = "AuthenticationController", tags = {"Authentication Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "login and registration controller.")
})
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;
    private final GeneratorResponseMessage generatorResponseMessage;

    @GetMapping("/statistics")
    public ResponseEntity<?> viewStatistics(@RequestParam(value = "begin", defaultValue = "1970-01-01") String begin,
                                            @RequestParam(value = "end", defaultValue = "2030-01-01") String end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StatisticsDto statistics = workoutService.getWorkoutStatistics(authentication.getName(), begin, end);
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/workout-info/{uuid}")
    public ResponseEntity<?> saveAdditionalInformation(@PathVariable("uuid") String uuid,
                                                       @Valid WorkoutInfoDto workoutInfoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Workout workout = workoutService.addWorkoutInfo(uuid, workoutInfoDto);
        return ResponseEntity.ok(workoutMapper.toDto(workout));
    }

    @GetMapping("/workouts")
    public ResponseEntity<?> viewWorkouts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<WorkoutDto> workouts = workoutService.getAllUserWorkouts(authentication.getName());
        return ResponseEntity.ok(workouts);
    }

    @PostMapping("/workouts")
    public ResponseEntity<?> saveWorkout(@Valid WorkoutDto workoutDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WorkoutDto workoutDtoSaved = workoutService.addWorkout(authentication.getName(), workoutDto);
        return ResponseEntity.ok(workoutDtoSaved);
    }

    @PutMapping("/workouts/{uuid}")
    public ResponseEntity<?> editWorkout(@PathVariable("uuid") String uuid,
                                         @Valid EditWorkout editWorkout, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Workout workout = workoutService.updateWorkout(authentication.getName(), uuid, editWorkout);
        return ResponseEntity.ok(workoutMapper.toDto(workout));

    }

    @DeleteMapping("/workouts/{uuid}")
    public ResponseEntity<?> deleteWorkouts(@PathVariable("uuid") String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        workoutService.deleteWorkout(authentication.getName(), uuid);
        return ResponseEntity.ok(new SuccessResponse("Данные успешно удалены!"));
    }
}
