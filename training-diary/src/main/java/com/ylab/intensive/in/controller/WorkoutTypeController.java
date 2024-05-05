package com.ylab.intensive.in.controller;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.*;
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
@RequestMapping("/training-diary/workouts/type")
@Api(value = "WorkoutTypeController", tags = {"Workout Type Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Controller for saving and viewing workout types.")
})
@RequiredArgsConstructor
public class WorkoutTypeController {
    private final WorkoutService workoutService;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final WorkoutTypeMapper workoutTypeMapper;

    @GetMapping
    @ApiOperation(value = "view user workout types", response = WorkoutTypeDto.class, responseContainer = "List")
    @Auditable(action = "Пользователь просмотрел свои типы тренировок.")
    public ResponseEntity<List<WorkoutTypeDto>> viewTypes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<WorkoutType> workoutTypes = workoutService.getAllType(authentication.getName());
        return ResponseEntity.ok(workoutTypes.stream()
                                            .map(workoutTypeMapper::toDto)
                                            .toList());
    }

    @PostMapping
    @ApiOperation(value = "save user workout type", response = WorkoutType.class)
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
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
