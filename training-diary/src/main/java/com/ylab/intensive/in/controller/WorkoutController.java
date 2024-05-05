package com.ylab.intensive.in.controller;

import com.ylab.intensive.aspects.annotation.Auditable;
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
        @Tag(name = "email and registration controller.")
})
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;
    private final GeneratorResponseMessage generatorResponseMessage;

    @GetMapping("/statistics")
    @Auditable(action = "Пользователь просмотрел статистики по тренировкам(количество потраченных калорий в разрезе времени)")
    public ResponseEntity<?> viewStatistics(@RequestParam(value = "begin", defaultValue = "1970-01-01") String begin,
                                            @RequestParam(value = "end", defaultValue = "2030-01-01") String end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StatisticsDto statistics = workoutService.getWorkoutStatistics(authentication.getName(), begin, end);
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/workout-info/{uuid}")
    @Auditable(action = "Пользователь добавил дополнительную информацию о тренировке uuid которого равен @uuid")
    public ResponseEntity<?> saveAdditionalInformation(@PathVariable("uuid") String uuid,
                                                       @RequestBody @Valid WorkoutInfoDto workoutInfoDto,
                                                       BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Workout workout = workoutService.addWorkoutInfo(authentication.getName(), uuid, workoutInfoDto);
        return ResponseEntity.ok(workoutMapper.toDto(workout));
    }

    @GetMapping("/workouts")
    @Auditable(action = "Пользователь просмотрел свои предыдущие тренировки.")
    public ResponseEntity<?> viewWorkouts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Workout> workouts = workoutService.getAllUserWorkouts(authentication.getName());
        return ResponseEntity.ok(workouts.stream().map(workoutMapper::toDto).toList());
    }

    @PostMapping("/workouts")
    @Auditable(action = "Пользователь добавил новую тренировку.")
    public ResponseEntity<?> saveWorkout(@RequestBody @Valid WorkoutDto workoutDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WorkoutDto workoutDtoSaved = workoutService.addWorkout(authentication.getName(), workoutDto);
        return ResponseEntity.ok(workoutDtoSaved);
    }

    @PutMapping("/workouts/{uuid}")
    @Auditable(action = "Пользователь редактировал тренировку по uuid=@uuid")
    public ResponseEntity<?> editWorkout(@PathVariable("uuid") String uuid,
                                         @RequestBody @Valid EditWorkout editWorkout, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Workout workout = workoutService.updateWorkout(authentication.getName(), uuid, editWorkout);
        return ResponseEntity.ok(workoutMapper.toDto(workout));

    }

    @DeleteMapping("/workouts/{uuid}")
    @Auditable(action = "Пользователь удалил тренировку по uuid=@uuid")
    public ResponseEntity<?> deleteWorkouts(@PathVariable("uuid") String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        workoutService.deleteWorkout(authentication.getName(), uuid);
        return ResponseEntity.ok(new SuccessResponse("Данные успешно удалены!"));
    }
}
