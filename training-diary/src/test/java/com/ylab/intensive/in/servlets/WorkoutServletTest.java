package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.EditWorkout;
import com.ylab.intensive.model.dto.SuccessResponse;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.service.ValidationService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.Converter;
import jakarta.servlet.ServletContext;
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
class WorkoutServletTest {

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private Converter converter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ValidationService validationService;
    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private WorkoutServlet workoutServlet;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);
    }

    @Test
    void testDoGet() throws IOException {
        String login = "user@example.com";
        List<WorkoutDto> workoutDtos = List.of(new WorkoutDto(), new WorkoutDto());

        when(request.getServletContext()).thenReturn(servletContext);
        when(authentication.getLogin()).thenReturn(login);
        when(workoutService.getAllUserWorkouts(login)).thenReturn(workoutDtos);
        when(converter.convertObjectToJson(workoutDtos)).thenReturn("[{\"workout\":\"data\"}]");

        workoutServlet.doGet(request, response);

        verify(workoutService).getAllUserWorkouts(login);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("[{\"workout\":\"data\"}]", stringWriter.toString());
    }

    @Test
    void testDoPost() throws IOException {
        String login = "user@example.com";
        WorkoutDto workoutDto = new WorkoutDto();
        WorkoutDto workoutDtoSaved = new WorkoutDto();

        when(request.getServletContext()).thenReturn(servletContext);
        when(authentication.getLogin()).thenReturn(login);
        when(converter.getRequestBody(request, WorkoutDto.class)).thenReturn(workoutDto);
        when(workoutService.addWorkout(login, workoutDto)).thenReturn(workoutDtoSaved);
        when(converter.convertObjectToJson(workoutDtoSaved)).thenReturn("{\"workout\":\"data\"}");
        when(validationService.validateAndReturnErrors(any())).thenReturn(Collections.emptyList());

        workoutServlet.doPost(request, response);

        verify(workoutService).addWorkout(login, workoutDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"workout\":\"data\"}", stringWriter.toString());
    }

    @Test
    void testDoPut_validUuid() throws IOException, ServletException {
        String login = "user@example.com";
        String uuid = "123e4567-e89b-12d3-a456-426614174001";
        EditWorkout editWorkout = new EditWorkout();
        Workout workout = new Workout();

        when(request.getServletContext()).thenReturn(servletContext);
        when(authentication.getLogin()).thenReturn(login);
        when(request.getPathInfo()).thenReturn("/" + uuid);

        when(converter.getRequestBody(request, EditWorkout.class)).thenReturn(editWorkout);
        when(workoutService.updateWorkout(login, uuid, editWorkout)).thenReturn(workout);
        when(workoutMapper.toDto(workout)).thenReturn(new WorkoutDto());
        when(converter.convertObjectToJson(any(WorkoutDto.class))).thenReturn("{\"workout\":\"data\"}");
        when(validationService.validateAndReturnErrors(any())).thenReturn(Collections.emptyList());

        workoutServlet.doPut(request, response);

        verify(workoutService).updateWorkout(login, uuid, editWorkout);
        verify(workoutMapper).toDto(workout);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"workout\":\"data\"}", stringWriter.toString());
    }

    @Test
    void testDoDelete_validUuid() throws IOException, ServletException {
        String login = "user@example.com";
        String uuid = "123e4567-e89b-12d3-a456-426614174001";

        when(request.getServletContext()).thenReturn(servletContext);
        when(authentication.getLogin()).thenReturn(login);
        when(request.getPathInfo()).thenReturn("/" + uuid);
        doNothing().when(workoutService).deleteWorkout(login, uuid);
        when(converter.convertObjectToJson(any(SuccessResponse.class))).thenReturn("{\"message\":\"Workout deleted successfully\"}");

        workoutServlet.doDelete(request, response);

        verify(workoutService).deleteWorkout(login, uuid);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"message\":\"Workout deleted successfully\"}", stringWriter.toString());
    }
}
