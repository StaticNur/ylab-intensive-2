package com.ylab.intensive.in.controller;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Api(tags = "Authorization and Registration")
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
    @ApiOperation(value = "User registration", response = UserDto.class)
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
    @Auditable(action = "Спортсмен зарегистрировался в системе.")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationDto registrationDto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        User user = userService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    /**
     * Authorizes a user.
     *
     * @param loginDto The SecurityRequest object containing user authorization information.
     * @return ResponseEntity containing the authorization token.
     */
    @PostMapping("/login")
    @ApiOperation(value = "User authorization", response = JwtResponse.class)
    @ApiResponse(code = 400, message = "Ошибка валидации. Подробности об ошибках содержатся в теле ответа.",
            response = CustomFieldError.class, responseContainer = "List")
    @Auditable(action = "Спортсмен авторизовался в системе.")
    public ResponseEntity<?> authorize(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        JwtResponse response = userService.login(loginDto);
        return ResponseEntity.ok(response);
    }
}
