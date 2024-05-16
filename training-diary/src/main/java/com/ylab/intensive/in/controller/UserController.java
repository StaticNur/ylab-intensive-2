package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.auditspringbootstarter.annotation.Auditable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequestMapping("/training-diary")
@Tag(name = "UserController", description = "User Controller")
@RequiredArgsConstructor
public class UserController {
    /**
     * Service for user-related operations
     */
    private final UserService userService;

    /**
     * Utility for generating custom error response messages
     */
    private final GeneratorResponseMessage generatorResponseMessage;

    /**
     * Service for workout-related operations
     */
    private final WorkoutService workoutService;

    /**
     * Mapper for converting User entities to DTOs
     */
    private final UserMapper userMapper;

    /**
     * Retrieves workouts for all users.
     *
     * @return ResponseEntity containing a list of UserDto objects with associated workouts
     */
    @GetMapping("/users/workouts")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "View workouts of all users", description = "Endpoint to view workouts of all users.")
    @Auditable(action = "Администратор просмотрел тренировки всех пользователей.")
    public ResponseEntity<?> viewTrainingsForAllUsers() {
        List<User> userList = userService.getAllUser();
        List<User> userWithWorkouts = workoutService.getAllUsersWorkouts(userList);

        return ResponseEntity.ok(userWithWorkouts.stream()
                .map(userMapper::toDto)
                .toList());
    }

    /**
     * Changes user rights.
     *
     * @param uuid                UUID of the user
     * @param changeUserRightsDto DTO containing user rights to be changed
     * @param bindingResult       BindingResult for validating the request body
     * @return ResponseEntity containing a SuccessResponse object
     */
    @PatchMapping("/users/{uuid}/access")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Change user rights", description = "Endpoint to change user rights.")
    @Auditable(action = "Пользователь изменил права пользователя по uuid=@uuid")
    public ResponseEntity<?> changeUserRights(@PathVariable("uuid") String uuid,
                                              @RequestBody @Valid ChangeUserRightsDto changeUserRightsDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        User user = userService.changeUserPermissions(uuid, changeUserRightsDto);

        return ResponseEntity.ok(new SuccessResponse("Права доступа успешно изменена! " +
                                                     "Теперь для данного пользователя " + user.getEmail()
                                                     + " требуется повторная авторизация для обновления токена."));
    }

    /**
     * Retrieves audit logs for the current user.
     *
     * @param page  Page number
     * @param count Number of records per page
     * @return ResponseEntity containing an AuditDto object
     */
    @GetMapping("/user/audit")
    @Operation(summary = "View the audit in action", description = "Endpoint to view the audit in action.")
    @Auditable(action = "Пользователь просмотрел свой аудит действий.")
    public ResponseEntity<AuditDto> viewAudit(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "count", defaultValue = "50") int count) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditDto audit = userService.getAudit(authentication.getName(), new Pageable(page, count));
        return ResponseEntity.ok(audit);
    }
}
