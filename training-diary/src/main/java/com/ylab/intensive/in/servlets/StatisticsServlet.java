package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.model.dto.StatisticsDto;
import com.ylab.intensive.security.Authentication;
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
 * The AuditServlet class is a servlet responsible for retrieving and displaying audit logs of user actions.
 * <p>
 * This servlet allows users to view audit logs of their actions by sending a GET request to the "/user/audit" endpoint.
 * The servlet retrieves the audit logs from the AuditService, converts them to DTOs, and returns them in JSON format.
 */
@WebServlet("/training-diary/statistics")
@ApplicationScoped
@NoArgsConstructor
public class StatisticsServlet extends HttpServlet {

    private WorkoutService workoutService;
    private Converter converter;

    @Inject
    public void inject(WorkoutService workoutService, Converter converter) {
        this.workoutService = workoutService;
        this.converter = converter;
    }

    /**
     * Handles GET requests to show audit logs of user actions.
     *
     * @param req  the HTTP servlet request
     * @param resp the HTTP servlet response
     * @throws IOException if an I/O error occurs during request processing
     */
    @Override
    @Loggable
    @Auditable
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
