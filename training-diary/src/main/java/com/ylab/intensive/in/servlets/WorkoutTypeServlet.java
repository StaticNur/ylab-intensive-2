package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.WorkoutTypeDto;
import com.ylab.intensive.model.entity.WorkoutType;
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
import java.util.List;

/**
 * The AuditServlet class is a servlet responsible for retrieving and displaying audit logs of user actions.
 * <p>
 * This servlet allows users to view audit logs of their actions by sending a GET request to the "/user/audit" endpoint.
 * The servlet retrieves the audit logs from the AuditService, converts them to DTOs, and returns them in JSON format.
 */
@WebServlet("/training-diary/workouts/type")
@ApplicationScoped
@NoArgsConstructor
public class WorkoutTypeServlet extends HttpServlet {
    private WorkoutService workoutService;
    private WorkoutTypeMapper workoutTypeMapper;
    private Converter converter;

    @Inject
    public void inject(WorkoutService workoutService, WorkoutTypeMapper workoutTypeMapper,
                       Converter converter) {
        this.workoutService = workoutService;
        this.workoutTypeMapper = workoutTypeMapper;
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
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        List<WorkoutType> workoutTypes = workoutService.getAllType(authentication.getLogin());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(workoutTypes.stream().map(workoutTypeMapper::toDto).toList()));

    }

    @Override
    @Loggable
    @Auditable
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        WorkoutTypeDto workoutTypeDto = converter.getRequestBody(req, WorkoutTypeDto.class);

        WorkoutType workoutType = workoutService.saveWorkoutType(authentication.getLogin(), workoutTypeDto.getType());
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter()
                .append(converter.convertObjectToJson(workoutTypeMapper.toDto(workoutType)));

    }
}
