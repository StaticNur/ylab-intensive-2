package com.ylab.intensive.in.servlets;

import com.ylab.intensive.model.dto.StatisticsDto;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Servlet for retrieving workout statistics.
 * <p>
 * This servlet handles HTTP GET requests to retrieve workout statistics within a specified date range.
 * It injects dependencies such as the workout service and converter for processing the requests.
 * </p>
 *
 * @since 1.0
 */
@WebServlet("/training-diary/statistics")
@ApplicationScoped
@NoArgsConstructor
public class StatisticsServlet extends HttpServlet {

    private WorkoutService workoutService;
    private Converter converter;

    /**
     * Injects dependencies into the servlet.
     *
     * @param workoutService the service for workout-related operations.
     * @param converter      the converter for converting objects to JSON.
     */
    @Inject
    public void inject(WorkoutService workoutService, Converter converter) {
        this.workoutService = workoutService;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String begin = req.getParameter("begin") == null ? "1970-01-01" : req.getParameter("begin");
        String end = req.getParameter("end") == null ? "2030-01-01" : req.getParameter("end");
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        StatisticsDto statistics = workoutService.getWorkoutStatistics(authentication.getLogin(), begin, end);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(statistics));
    }
}
