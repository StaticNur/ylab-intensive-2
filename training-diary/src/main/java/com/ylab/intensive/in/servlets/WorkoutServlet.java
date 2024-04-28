package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.EditWorkout;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.dto.SuccessResponse;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.security.Authentication;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The AuditServlet class is a servlet responsible for retrieving and displaying audit logs of user actions.
 * <p>
 * This servlet allows users to view audit logs of their actions by sending a GET request to the "/user/audit" endpoint.
 * The servlet retrieves the audit logs from the AuditService, converts them to DTOs, and returns them in JSON format.
 */
@WebServlet("/training-diary/workouts/*")
@ApplicationScoped
@NoArgsConstructor
public class WorkoutServlet extends HttpServlet {
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
        List<WorkoutDto> workouts = workoutService.getAllUserWorkouts(authentication.getLogin());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(workouts));

    }

    @Override
    @Loggable
    @Auditable
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        WorkoutDto workoutDto = converter.getRequestBody(req, WorkoutDto.class);

        WorkoutDto workoutDtoSaved = workoutService.addWorkout(authentication.getLogin(), workoutDto);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(workoutDtoSaved));
    }

    @Override
    @Loggable
    @Auditable
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        String pathInfo = req.getPathInfo(); // "/workouts/{uuid}"
        String uuid = pathInfo.substring(1, pathInfo.length()); // "{uuid}"
        Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

        if (pattern.matcher(uuid).matches()) {
            EditWorkout editWorkout = converter.getRequestBody(req, EditWorkout.class);
            Workout workout = workoutService.updateWorkout(authentication.getLogin(), uuid, editWorkout);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter()
                    .append(converter.convertObjectToJson(workoutMapper.toDto(workout)));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter()
                    .append(converter.convertObjectToJson(new ExceptionResponse("Not found endpoint."
                                                                                + " Maybe the 'uuid' is not correct")));
        }

    }

    @Override
    @Loggable
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        String pathInfo = req.getPathInfo(); // "/workouts/{uuid}"
        String[] pathParts = pathInfo.split("/");
        String uuid = pathParts[pathParts.length - 1]; // "{uuid}"
        Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

        if (pattern.matcher(uuid).matches()) {
            workoutService.deleteWorkout(authentication.getLogin(), uuid);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter()
                    .append(converter.convertObjectToJson(new SuccessResponse("Данные успешно удалены!")));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter()
                    .append(converter.convertObjectToJson(new ExceptionResponse("Not found endpoint."
                                                                                + " Maybe the 'uuid' is not correct")));
        }
    }
}






















