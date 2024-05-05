package com.ylab.intensive.in.controller;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.WorkoutType;
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
@RequestMapping("/training-diary/workouts/type")
@Api(value = "AuthenticationController", tags = {"Authentication Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "email and registration controller.")
})
@RequiredArgsConstructor
public class WorkoutTypeController {
    private final WorkoutService workoutService;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final WorkoutTypeMapper workoutTypeMapper;

    @GetMapping
    @Auditable(action = "Пользователь просмотрел свои типы тренировок.")
    public ResponseEntity<?> viewTypes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<WorkoutType> workoutTypes = workoutService.getAllType(authentication.getName());
        return ResponseEntity.ok(workoutTypes.stream()
                                            .map(workoutTypeMapper::toDto)
                                            .toList());
    }

    @PostMapping
    @Auditable(action = "Пользователь добавил новый тип тренировки.")
    public ResponseEntity<?> saveType(@RequestBody @Valid WorkoutTypeDto workoutTypeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WorkoutType workoutType = workoutService.saveWorkoutType(authentication.getName(), workoutTypeDto.getType());
        return ResponseEntity.ok(workoutTypeMapper.toDto(workoutType));
    }
}
