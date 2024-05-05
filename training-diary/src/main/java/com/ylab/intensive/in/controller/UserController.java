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
 * Controller class for handling authentication operations.
 */
@RestController
@RequestMapping("/training-diary")
@Api(value = "UserController", tags = {"User Controller"})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final WorkoutService workoutService;
    private final UserMapper userMapper;

    @GetMapping("/users/workouts")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "view workouts of all users", response = WorkoutDto.class, responseContainer = "List")
    @Auditable(action = "Пользователь просмотрел тренировки всех пользователей.")
    public ResponseEntity<?> viewTrainingsForAllUsers() {
        List<User> userList = userService.getAllUser();
        List<User> userWithWorkouts = workoutService.getAllUsersWorkouts(userList);

        List<UserDto> userDtolist = userWithWorkouts.stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok(userDtolist);
    }

    @PatchMapping("/users/{uuid}/access")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "change user rights", response = SuccessResponse.class)
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

    @GetMapping("/user/audit")
    @ApiOperation(value = "view the audit in action", response = AuditDto.class)
    @Auditable(action = "Пользователь просмотрел свой аудит действий.")
    public ResponseEntity<AuditDto> viewAudit(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "count", defaultValue = "50") int count) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditDto audit = userService.getAudit(authentication.getName(), new Pageable(page, count));
        return ResponseEntity.ok(audit);
    }
}
