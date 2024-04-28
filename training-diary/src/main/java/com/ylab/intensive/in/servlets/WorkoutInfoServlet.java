package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.dto.WorkoutInfoDto;
import com.ylab.intensive.model.entity.Workout;
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
import java.util.regex.Pattern;

/**
 * The AuditServlet class is a servlet responsible for retrieving and displaying audit logs of user actions.
 * <p>
 * This servlet allows users to view audit logs of their actions by sending a GET request to the "/user/audit" endpoint.
 * The servlet retrieves the audit logs from the AuditService, converts them to DTOs, and returns them in JSON format.
 */
@WebServlet("/training-diary/workout-info/*")
@ApplicationScoped
@NoArgsConstructor
public class WorkoutInfoServlet extends HttpServlet {
    private WorkoutService workoutService;
    private WorkoutMapper workoutMapper;
    private Converter converter;

    @Inject
    public void inject(WorkoutService workoutService, WorkoutMapper workoutMapper,
                       Converter converter) {
        this.workoutService = workoutService;
        this.workoutMapper = workoutMapper;
        this.converter = converter;
    }

    @Override
    @Loggable
    @Auditable
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String uuid = pathParts[pathParts.length - 1];
        Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

        if (pattern.matcher(uuid).matches()) {
            WorkoutInfoDto workoutInfoDto = converter.getRequestBody(req, WorkoutInfoDto.class);

            Workout workout = workoutService.addWorkoutInfo(uuid, workoutInfoDto);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter()
                    .append(converter.convertObjectToJson(workoutMapper.toDto(workout)));
        } else {
            sendErrorMessage(resp);
        }
    }

    private void sendErrorMessage(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
                .append(converter.convertObjectToJson(new ExceptionResponse("Not found endpoint."
                                                                            + " Maybe the 'uuid' is not correct")));
    }
}
