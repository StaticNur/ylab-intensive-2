package com.ylab.intensive.in.controller;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for managing user-related operations.
 */
@RestController
@RequestMapping("/training-diary")
@Api(value = "UserController", tags = {"User Controller"})
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
    @ApiOperation(value = "view workouts of all users", response = WorkoutDto.class, responseContainer = "List",
            authorizations = {
                    @Authorization(value = "JWT")
            })
    @Auditable(action = "Пользователь просмотрел тренировки всех пользователей.")
    public ResponseEntity<?> viewTrainingsForAllUsers() {
        List<User> userList = userService.getAllUser();
        List<User> userWithWorkouts = workoutService.getAllUsersWorkouts(userList);

        List<UserDto> userDtolist = userWithWorkouts.stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok(userDtolist);
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
    @ApiOperation(value = "change user rights", response = SuccessResponse.class,
            authorizations = {
                    @Authorization(value = "JWT")
            })
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
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
    @ApiOperation(value = "view the audit in action", response = AuditDto.class,
            authorizations = {
                    @Authorization(value = "JWT")
            })
    @Auditable(action = "Пользователь просмотрел свой аудит действий.")
    public ResponseEntity<AuditDto> viewAudit(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "count", defaultValue = "50") int count) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditDto audit = userService.getAudit(authentication.getName(), new Pageable(page, count));
        return ResponseEntity.ok(audit);
    }
}
