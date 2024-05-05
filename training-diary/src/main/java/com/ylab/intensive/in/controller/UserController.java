package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
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
@Api(value = "AuthenticationController", tags = {"Authentication Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "login and registration controller.")
})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final GeneratorResponseMessage generatorResponseMessage;
    private final WorkoutService workoutService;
    private final UserMapper userMapper;

    @GetMapping("/users/workouts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> viewTrainingsForAllUsers() {
        List<User> userList = userService.getAllUser();
        List<User> userWithWorkouts = workoutService.getAllUsersWorkouts(userList);

        List<UserDto> userDtolist = userWithWorkouts.stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok(userDtolist);
    }

    @PatchMapping("/users/{uuid}/access")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    public ResponseEntity<?> viewAudit(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "count", defaultValue = "10") int count) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditDto audit = userService.getAudit(authentication.getName(), new Pageable(page, count));
        return ResponseEntity.ok(audit);
    }
}
