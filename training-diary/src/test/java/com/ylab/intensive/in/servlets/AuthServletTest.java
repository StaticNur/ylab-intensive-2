/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.ValidationService;
import com.ylab.intensive.util.Converter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServletTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Converter converter;

    @Mock
    private ValidationService validationService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthServlet authServlet;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @Test
    void testRegister() throws IOException {
        RegistrationDto registrationDto = new RegistrationDto("test@email.com", "password", Role.USER);
        User user = new User();
        UserDto userDto = new UserDto();
        userDto.setEmail("email");
        userDto.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        userDto.setRole(Role.USER);
        userDto.setWorkouts(Collections.emptyList());

        when(converter.getRequestBody(request, RegistrationDto.class)).thenReturn(registrationDto);
        when(userService.registerUser(registrationDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(converter.convertObjectToJson(userDto)).thenReturn("{\"email\":\"email\",\"uuid\":\"123e4567-e89b-12d3-a456-426614174001\",\"role\":\"USER\",\"workouts\":[]}");
        when(request.getPathInfo()).thenReturn("/registration");
        when(validationService.validateAndReturnErrors(any())).thenReturn(Collections.emptyList());

        authServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        assertEquals("{\"email\":\"email\",\"uuid\":\"123e4567-e89b-12d3-a456-426614174001\",\"role\":\"USER\",\"workouts\":[]}"
                , stringWriter.toString());
    }

    @Test
    void testLogin() throws IOException {
        LoginDto loginDto = new LoginDto("test@email.com", "password");
        JwtResponse jwtResponse = new JwtResponse("token", "refreshToken", "test@email.com");
        when(converter.getRequestBody(request, LoginDto.class)).thenReturn(loginDto);
        when(userService.email(loginDto)).thenReturn(jwtResponse);
        when(converter.convertObjectToJson(jwtResponse)).thenReturn("{\"jwt\":\"data\"}");
        when(request.getPathInfo()).thenReturn("/email");
        when(validationService.validateAndReturnErrors(any())).thenReturn(Collections.emptyList());

        authServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"jwt\":\"data\"}", stringWriter.toString());
    }

    @Test
    void testInvalidEndpoint() throws IOException {
        when(request.getPathInfo()).thenReturn("/invalid");
        when(converter.convertObjectToJson(any(ExceptionResponse.class))).thenReturn("{\"error\":\"Not found endpoint.\"}");

        authServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("{\"error\":\"Not found endpoint.\"}", stringWriter.toString());
    }
}
*/
