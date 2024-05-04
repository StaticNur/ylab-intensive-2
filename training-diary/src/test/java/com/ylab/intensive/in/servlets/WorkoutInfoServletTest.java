/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.dto.WorkoutInfoDto;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.service.WorkoutService;
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
class WorkoutInfoServletTest {

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private Converter converter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private WorkoutInfoServlet workoutInfoServlet;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @Test
    void testDoPost_validUuid() throws IOException {
        String uuid = "123e4567-e89b-12d3-a456-426614174001";
        WorkoutInfoDto workoutInfoDto = new WorkoutInfoDto();
        Workout workout = new Workout();

        when(request.getPathInfo()).thenReturn("/workout/" + uuid);
        when(converter.getRequestBody(request, WorkoutInfoDto.class)).thenReturn(workoutInfoDto);
        when(workoutService.addWorkoutInfo(uuid, workoutInfoDto)).thenReturn(workout);
        when(workoutMapper.toDto(workout)).thenReturn(new WorkoutDto());
        when(converter.convertObjectToJson(any(WorkoutDto.class))).thenReturn("{\"workout\":\"data\"}");

        workoutInfoServlet.doPost(request, response);

        verify(workoutService).addWorkoutInfo(uuid, workoutInfoDto);
        verify(workoutMapper).toDto(workout);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"workout\":\"data\"}", stringWriter.toString());
    }

    @Test
    void testDoPost_invalidUuid() throws IOException {
        String invalidUuid = "invalid-uuid";

        when(request.getPathInfo()).thenReturn("/workout/" + invalidUuid);
        when(converter.convertObjectToJson(any(ExceptionResponse.class)))
                .thenReturn("{\"error\":\"Not found endpoint. Maybe the 'uuid' is not correct\"}");

        workoutInfoServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("{\"error\":\"Not found endpoint. Maybe the 'uuid' is not correct\"}", stringWriter.toString());
    }
}
*/
