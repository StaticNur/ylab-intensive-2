/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.model.dto.StatisticsDto;
import com.ylab.intensive.model.Authentication;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServletTest {

    @Mock
    private WorkoutService workoutService;

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
    private StatisticsServlet statisticsServlet;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("authentication")).thenReturn(authentication);
    }

    @Test
    void testDoGet_withDefaultDates() throws IOException {
        when(authentication.getLogin()).thenReturn("testUser");
        StatisticsDto statisticsDto = new StatisticsDto();
        when(workoutService.getWorkoutStatistics("testUser", "1970-01-01", "2030-01-01")).thenReturn(statisticsDto);
        when(converter.convertObjectToJson(statisticsDto)).thenReturn("{\"statistics\":\"data\"}");

        statisticsServlet.doGet(request, response);

        verify(workoutService).getWorkoutStatistics("testUser", "1970-01-01", "2030-01-01");
        verify(converter).convertObjectToJson(statisticsDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"statistics\":\"data\"}", stringWriter.toString());
    }

    @Test
    void testDoGet_withCustomDates() throws IOException {
        when(request.getParameter("begin")).thenReturn("01-01-2022");
        when(request.getParameter("end")).thenReturn("31-12-2022");
        when(authentication.getLogin()).thenReturn("testUser");
        StatisticsDto statisticsDto = new StatisticsDto();
        when(workoutService.getWorkoutStatistics("testUser", "01-01-2022", "31-12-2022")).thenReturn(statisticsDto);
        when(converter.convertObjectToJson(statisticsDto)).thenReturn("{\"statistics\":\"data\"}");

        statisticsServlet.doGet(request, response);

        verify(workoutService).getWorkoutStatistics("testUser", "01-01-2022", "31-12-2022");
        verify(converter).convertObjectToJson(statisticsDto);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("{\"statistics\":\"data\"}", stringWriter.toString());
    }
}
*/
