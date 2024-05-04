/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.ChangeUserRightsDto;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.dto.SuccessResponse;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.ValidationService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.Converter;
import jakarta.servlet.ServletException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServletTest {

    @Mock
    private UserService userService;

    @Mock
    private ValidationService validationServicer;
    @Mock
    private WorkoutService workoutService;

    @Mock
    private Converter converter;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserServlet userServlet;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @Test
    void testDoGet_getAllUsersWithWorkouts() throws IOException {
        List<User> userList = Collections.emptyList();
        List<User> userWithWorkouts = Collections.emptyList();

        when(request.getPathInfo()).thenReturn("/workouts");
        when(userService.getAllUser()).thenReturn(userList);
        when(workoutService.getAllUsersWorkouts(userList)).thenReturn(userWithWorkouts);
        when(converter.convertObjectToJson(any())).thenReturn("[]");

        userServlet.doGet(request, response);

        verify(userService).getAllUser();
        verify(workoutService).getAllUsersWorkouts(userList);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("[]", stringWriter.toString());
    }

    @Test
    void testDoGet_invalidEndpoint() throws IOException {
        when(request.getPathInfo()).thenReturn("/invalid");
        when(converter.convertObjectToJson(any(ExceptionResponse.class))).thenReturn("{\"message\":\"Not found endpoint.\"}");

        userServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("{\"message\":\"Not found endpoint.\"}", stringWriter.toString());
    }

    @Test
    void testDoPatch_changeUserPermissions() throws IOException, ServletException {
        String uuid = "123e4567-e89b-12d3-a456-426614174001";
        ChangeUserRightsDto changeUserRightsDto = new ChangeUserRightsDto(Role.USER);
        User user = new User();
        user.setEmail("email");

        when(request.getMethod()).thenReturn("PATCH");
        when(request.getPathInfo()).thenReturn("/" + uuid + "/access");
        when(converter.getRequestBody(request, ChangeUserRightsDto.class)).thenReturn(changeUserRightsDto);
        when(userService.changeUserPermissions(uuid, changeUserRightsDto)).thenReturn(user);
        when(validationServicer.validateAndReturnErrors(any())).thenReturn(Collections.emptyList());
        when(converter.convertObjectToJson(any(SuccessResponse.class)))
                .thenReturn("{\"message\":\"Права доступа успешно изменена! Теперь для данного пользователя " +
                             "email требуется повторная авторизация для обновления токена.\"}");

        userServlet.service(request, response);

        verify(userService).changeUserPermissions(uuid, changeUserRightsDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"message\":\"Права доступа успешно изменена! Теперь для данного пользователя " +
                     "email требуется повторная авторизация для обновления токена.\"}", stringWriter.toString());
    }
}
*/
