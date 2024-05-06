package com.ylab.intensive.in.controller;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import com.ylab.intensive.util.validation.annotation.ValidUUID;
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
 * Controller class for managing workout-related operations.
 */
@RestController
@RequestMapping("/training-diary")
@Api(value = "WorkoutController", tags = {"Workout Controller"})
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
     * Retrieves training statistics (number of calories burned over time).
     *
     * @param begin Start date for the statistics (default: "1970-01-01")
     * @param end   End date for the statistics (default: "2030-01-01")
     * @return ResponseEntity containing a StatisticsDto object
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "obtaining training statistics (number of calories burned over time)",
            response = StatisticsDto.class,
            authorizations = {
                    @Authorization(value="JWT")
            })
    @Auditable(action = "Пользователь просмотрел статистики по тренировкам(количество потраченных калорий в разрезе времени)")
    public ResponseEntity<StatisticsDto> viewStatistics(@RequestParam(value = "begin", defaultValue = "1970-01-01") String begin,
                                                        @RequestParam(value = "end", defaultValue = "2030-01-01") String end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StatisticsDto statistics = workoutService.getWorkoutStatistics(authentication.getName(), begin, end);
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
    @ApiOperation(value = "add additional information about the workout", response = WorkoutDto.class,
            authorizations = {
                    @Authorization(value="JWT")
            })
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
    @Auditable(action = "Пользователь добавил дополнительную информацию о тренировке uuid которого равен @uuid")
    public ResponseEntity<?> saveAdditionalInformation(@PathVariable("uuid") @ValidUUID String uuid,
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

    /**
     * Retrieves the previous workouts of the authenticated user.
     *
     * @return ResponseEntity containing a list of WorkoutDto objects representing the user's workouts
     */
    @GetMapping("/workouts")
    @ApiOperation(value = "view your previous workouts", response = WorkoutDto.class, responseContainer = "List",
            authorizations = {
                    @Authorization(value="JWT")
            })
    @Auditable(action = "Пользователь просмотрел свои предыдущие тренировки.")
    public ResponseEntity<List<WorkoutDto>> viewWorkouts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Workout> workouts = workoutService.getAllWorkoutsByUser(authentication.getName());
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
    @ApiOperation(value = "save user workout", response = WorkoutDto.class,
            authorizations = {
                    @Authorization(value="JWT")
            })
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

    /**
     * Edits an existing workout for the authenticated user.
     *
     * @param uuid          UUID of the workout to be edited
     * @param editWorkout   DTO containing details of the edited workout
     * @param bindingResult BindingResult for validating the request body
     * @return ResponseEntity containing the edited WorkoutDto object
     */
    @PutMapping("/workouts/{uuid}")
    @ApiOperation(value = "edit user workout", response = WorkoutDto.class,
            authorizations = {
                    @Authorization(value="JWT")
            })
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
    @Auditable(action = "Пользователь редактировал тренировку по uuid=@uuid")
    public ResponseEntity<?> editWorkout(@PathVariable("uuid") @ValidUUID String uuid,
                                         @RequestBody @Valid EditWorkout editWorkout, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Workout workout = workoutService.updateWorkout(authentication.getName(), uuid, editWorkout);
        return ResponseEntity.ok(workoutMapper.toDto(workout));

    }

    /**
     * Deletes a workout for the authenticated user.
     *
     * @param uuid UUID of the workout to be deleted
     * @return ResponseEntity containing a SuccessResponse object
     */
    @DeleteMapping("/workouts/{uuid}")
    @ApiOperation(value = "delete a user's workout", response = SuccessResponse.class,
            authorizations = {
                    @Authorization(value="JWT")
            })
    @Auditable(action = "Пользователь удалил тренировку по uuid=@uuid")
    public ResponseEntity<SuccessResponse> deleteWorkouts(@PathVariable("uuid") @ValidUUID String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        workoutService.deleteWorkout(authentication.getName(), uuid);
        return ResponseEntity.ok(new SuccessResponse("Данные успешно удалены!"));
    }
}
