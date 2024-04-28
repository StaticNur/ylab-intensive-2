package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.service.UserService;
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

   /* @Test
    void testRegister() throws IOException {
        RegistrationDto registrationDto = new RegistrationDto("test@email.com", "password", Role.USER);
        User user = new User();
        UserDto userDto = new UserDto();
        when(converter.getRequestBody(request, RegistrationDto.class)).thenReturn(registrationDto);
        when(userService.registerUser(registrationDto)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(converter.convertObjectToJson(user)).thenReturn("{\"user\":\"data\"}");

        authServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"user\":\"data\"}", stringWriter.toString());
    }

    @Test
    void testLogin() throws IOException {
        LoginDto loginDto = new LoginDto("test@email.com", "password");
        JwtResponse jwtResponse = new JwtResponse("token", "refreshToken", "test@email.com");
        when(converter.getRequestBody(request, LoginDto.class)).thenReturn(loginDto);
        when(userService.login(loginDto)).thenReturn(jwtResponse);
        when(converter.convertObjectToJson(jwtResponse)).thenReturn("{\"jwt\":\"data\"}");

        authServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"jwt\":\"data\"}", stringWriter.toString());
    }
*/
    @Test
    void testInvalidEndpoint() throws IOException {
        when(request.getPathInfo()).thenReturn("/invalid");
        when(converter.convertObjectToJson(any(ExceptionResponse.class))).thenReturn("{\"error\":\"Not found endpoint.\"}");

        authServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("{\"error\":\"Not found endpoint.\"}", stringWriter.toString());
    }
}
