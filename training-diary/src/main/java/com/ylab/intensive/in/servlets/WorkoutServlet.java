package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.service.ValidationService;
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
 * Servlet for handling workout-related operations.
 * <p>
 * This servlet handles HTTP GET, POST, PUT, and DELETE requests for managing workouts.
 * It injects dependencies such as the workout service, validation service,
 * workout mapper, and converter for processing the requests.
 * </p>
 *
 * @since 1.0
 */
@WebServlet("/training-diary/workouts/*")
@ApplicationScoped
@NoArgsConstructor
public class WorkoutServlet extends HttpServlet {
    private WorkoutService workoutService;
    private ValidationService validationService;
    private WorkoutMapper workoutMapper;
    private Converter converter;

    /**
     * Injects dependencies into the servlet.
     *
     * @param workoutService    the service for workout-related operations.
     * @param validationService the service for validating user input.
     * @param workoutMapper     the mapper for mapping workout entities to DTOs.
     * @param converter         the converter for converting objects to JSON.
     */
    @Inject
    public void inject(WorkoutService workoutService, ValidationService validationService,
                       WorkoutMapper workoutMapper, Converter converter) {
        this.workoutService = workoutService;
        this.validationService = validationService;
        this.workoutMapper = workoutMapper;
        this.converter = converter;
    }

    /**
     * Handles HTTP GET requests to retrieve all workouts for the authenticated user.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        List<WorkoutDto> workouts = workoutService.getAllUserWorkouts(authentication.getLogin());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(workouts));
    }

    /**
     * Handles HTTP POST requests to add a new workout for the authenticated user.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        WorkoutDto workoutDto = converter.getRequestBody(req, WorkoutDto.class);

        List<ValidationError> validationErrors = validationService.validateAndReturnErrors(workoutDto);

        if (validationErrors.isEmpty()) {
            WorkoutDto workoutDtoSaved = workoutService.addWorkout(authentication.getLogin(), workoutDto);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter()
                    .append(converter.convertObjectToJson(workoutDtoSaved));
        } else {
            sendErrorMessage(resp, validationErrors);
        }
    }

    /**
     * Handles HTTP PUT requests to update an existing workout for the authenticated user.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        String pathInfo = req.getPathInfo();
        String uuid = pathInfo.substring(1, pathInfo.length());

        if (isValidUUID(uuid)) {
            EditWorkout editWorkout = converter.getRequestBody(req, EditWorkout.class);
            List<ValidationError> validationErrors = validationService.validateAndReturnErrors(editWorkout);

            if (validationErrors.isEmpty()) {
                Workout workout = workoutService.updateWorkout(authentication.getLogin(), uuid, editWorkout);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter()
                        .append(converter.convertObjectToJson(workoutMapper.toDto(workout)));
            } else {
                sendErrorMessage(resp, validationErrors);
            }
        } else {
            sendErrorMessage(resp, new ExceptionResponse("Not found endpoint. Maybe the 'uuid' is not correct"));
        }
    }

    /**
     * Handles HTTP DELETE requests to delete an existing workout for the authenticated user.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        String pathInfo = req.getPathInfo();
        String uuid = pathInfo.substring(1, pathInfo.length());

        if (isValidUUID(uuid)) {
            workoutService.deleteWorkout(authentication.getLogin(), uuid);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter()
                    .append(converter.convertObjectToJson(new SuccessResponse("Данные успешно удалены!")));
        } else {
            sendErrorMessage(resp, new ExceptionResponse("Not found endpoint. Maybe the 'uuid' is not correct"));
        }
    }

    /**
     * Checks if the provided UUID is in a valid format.
     *
     * @param uuid the UUID string to validate.
     * @return true if the UUID is in a valid format; false otherwise.
     */
    private boolean isValidUUID(String uuid) {
        Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
        return pattern.matcher(uuid).matches();
    }

    /**
     * Sends error message as response.
     *
     * @param resp   the HTTP servlet response.
     * @param object the object representing the error response.
     * @throws IOException if an I/O error occurs while sending the response.
     */
    private void sendErrorMessage(HttpServletResponse resp, Object object) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
                .append(converter.convertObjectToJson(object));
    }
}

