package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.WorkoutTypeDto;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.service.ValidationService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.Converter;
import jakarta.servlet.ServletContext;
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
class WorkoutTypeServletTest {

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutTypeMapper workoutTypeMapper;
    @Mock
    private ValidationService validationService;
    @Mock
    private Converter converter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private WorkoutTypeServlet workoutTypeServlet;

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
        List<WorkoutType> workoutTypes = List.of(new WorkoutType(), new WorkoutType());
        List<WorkoutTypeDto> workoutTypeDtos = List.of(new WorkoutTypeDto("type1"), new WorkoutTypeDto("type1"));

        when(request.getServletContext()).thenReturn(servletContext);
        when(authentication.getLogin()).thenReturn(login);
        when(workoutService.getAllType(login)).thenReturn(workoutTypes);
        when(workoutTypeMapper.toDto(any(WorkoutType.class))).thenReturn(new WorkoutTypeDto("type1"));
        when(converter.convertObjectToJson(workoutTypeDtos)).thenReturn("[{\"type\":\"data\"}]");

        workoutTypeServlet.doGet(request, response);

        verify(workoutService).getAllType(login);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("[{\"type\":\"data\"}]", stringWriter.toString());
    }


    @Test
    void testDoPost() throws IOException {
        String login = "user@example.com";
        WorkoutTypeDto workoutTypeDto = new WorkoutTypeDto("Running");
        WorkoutType workoutType = new WorkoutType();
        workoutType.setType("Running");

        when(request.getServletContext()).thenReturn(servletContext);
        when(authentication.getLogin()).thenReturn(login);
        when(converter.getRequestBody(request, WorkoutTypeDto.class)).thenReturn(workoutTypeDto);
        when(workoutService.saveWorkoutType(login, workoutTypeDto.getType())).thenReturn(workoutType);
        when(workoutTypeMapper.toDto(workoutType)).thenReturn(workoutTypeDto);
        when(converter.convertObjectToJson(workoutTypeDto)).thenReturn("{\"type\":\"Running\"}");
        when(validationService.validateAndReturnErrors(any())).thenReturn(Collections.emptyList());

        workoutTypeServlet.doPost(request, response);

        verify(workoutService).saveWorkoutType(login, workoutTypeDto.getType());
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        assertEquals("{\"type\":\"Running\"}", stringWriter.toString());
    }
}
