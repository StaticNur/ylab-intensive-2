package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for handling authentication operations.
 */
@RestController
@Api(value = "AuditController", tags = {"Audit Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Audit Controller")
})
@RequestMapping("/training-diary/auth")
@RequiredArgsConstructor
public class SecurityController {

    /**
     * The service for user-related operations.
     */
    private final UserService userService;
    private final GeneratorResponseMessage generatorResponseMessage;

    /**
     * The mapper for mapping user entities to DTOs.
     */
    private final UserMapper userMapper;

    /**
     * Registers a new user.
     *
     * @param registrationDto The SecurityRequest object containing user registration information.
     * @return ResponseEntity containing the registered User object.
     */
    @PostMapping("/registration")
    @ApiOperation(value = "returns Audit of user actions", responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Пользователь не найден. Подробности об ошибках содержатся в теле ответа.", response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "Внутренняя ошибка сервера. Подробности об ошибке содержатся в теле ответа.", response = ExceptionResponse.class)
    })
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationDto registrationDto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        User user = userService.registerUser(registrationDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * Authorizes a user.
     *
     * @param loginDto The SecurityRequest object containing user authorization information.
     * @return ResponseEntity containing the authorization token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authorize(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        JwtResponse response = userService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
