package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.ylab.auditspringbootstarter.annotation.Auditable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for handling authentication operations.
 */
@RestController
@Tag(name = "SecurityController", description = "Authorization and Registration")
@RequestMapping("/training-diary/auth")
@RequiredArgsConstructor
public class SecurityController {

    /**
     * The service for user-related operations.
     */
    private final UserService userService;

    /**
     * Utility for generating custom error response messages
     */
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
    @Operation(summary = "User registration",
            description = "Endpoint for user registration.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Validation Error. Details about errors are in the response body.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomFieldError.class))))
            })
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
    @Operation(summary = "User authorization",
            description = "Endpoint for user authorization.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation Error. Details about errors are in the response body.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomFieldError.class))))
            })
    @Auditable(action = "Спортсмен авторизовался в системе.")
    public ResponseEntity<?> authorize(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        JwtResponse response = userService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    /**
     * update the token a user.
     *
     * @param refreshTokenDto The SecurityRequest object containing user refresh token.
     * @return ResponseEntity containing the authorization token.
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "User refresh token",
            description = "Endpoint to refresh user token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation Error. Details about errors are in the response body.",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CustomFieldError.class))))
            })
    @Auditable(action = "Спортсмен продлевает срок службы токена.")
    public ResponseEntity<?> extendTheLifeOfTheToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> customFieldErrors = generatorResponseMessage.generateErrorMessage(bindingResult);
            return ResponseEntity.badRequest().body(customFieldErrors);
        }
        JwtResponse response = userService.updateToken(refreshTokenDto.refreshToken().trim());
        return ResponseEntity.ok(response);
    }
}
