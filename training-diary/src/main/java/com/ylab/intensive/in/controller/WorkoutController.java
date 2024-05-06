package com.ylab.intensive.in.controller;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Api(value = "WorkoutController", tags = {"Workout Controller"})
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;
    private final WorkoutMapper workoutMapper;
    private final GeneratorResponseMessage generatorResponseMessage;

    @GetMapping("/statistics")
    @ApiOperation(value = "obtaining training statistics (number of calories burned over time)", response = StatisticsDto.class)
    @Auditable(action = "Пользователь просмотрел статистики по тренировкам(количество потраченных калорий в разрезе времени)")
    public ResponseEntity<StatisticsDto> viewStatistics(@RequestParam(value = "begin", defaultValue = "1970-01-01") String begin,
                                            @RequestParam(value = "end", defaultValue = "2030-01-01") String end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StatisticsDto statistics = workoutService.getWorkoutStatistics(authentication.getName(), begin, end);
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/workout-info/{uuid}")
    @ApiOperation(value = "add additional information about the workout", response = WorkoutDto.class)
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
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
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutMapper.toDto(workout));
    }

    @GetMapping("/workouts")
    @ApiOperation(value = "view your previous workouts", response = WorkoutDto.class, responseContainer = "List")
    @Auditable(action = "Пользователь просмотрел свои предыдущие тренировки.")
    public ResponseEntity<List<WorkoutDto>> viewWorkouts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Workout> workouts = workoutService.getAllWorkoutsByUser(authentication.getName());
        return ResponseEntity.ok(workouts.stream().map(workoutMapper::toDto).toList());
    }

    @PostMapping("/workouts")
    @ApiOperation(value = "save user workout", response = WorkoutDto.class)
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
    @Auditable(action = "Пользователь добавил новую тренировку.")
    public ResponseEntity<?> saveWorkout(@RequestBody @Valid WorkoutDto workoutDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WorkoutDto workoutDtoSaved = workoutService.addWorkout(authentication.getName(), workoutDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutDtoSaved);
    }

    @PutMapping("/workouts/{uuid}")
    @ApiOperation(value = "edit user workout", response = WorkoutDto.class)
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
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
    @ApiOperation(value = "delete a user's workout", response = SuccessResponse.class)
    @Auditable(action = "Пользователь удалил тренировку по uuid=@uuid")
    public ResponseEntity<SuccessResponse> deleteWorkouts(@PathVariable("uuid") String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        workoutService.deleteWorkout(authentication.getName(), uuid);
        return ResponseEntity.ok(new SuccessResponse("Данные успешно удалены!"));
    }
}
