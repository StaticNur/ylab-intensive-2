/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.ValidationError;
import com.ylab.intensive.model.dto.WorkoutTypeDto;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.model.Authentication;
import com.ylab.intensive.service.ValidationService;
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

*/
/**
 * Servlet for handling workout type-related operations.
 * <p>
 * This servlet handles HTTP GET and POST requests for managing workout types.
 * It injects dependencies such as the workout service, validation service, workout type mapper, and converter for processing the requests.
 * </p>
 *
 * @since 1.0
 *//*

@WebServlet("/training-diary/workouts/type")
@ApplicationScoped
@NoArgsConstructor
public class WorkoutTypeServlet extends HttpServlet {
    private WorkoutService workoutService;
    private ValidationService validationService;
    private WorkoutTypeMapper workoutTypeMapper;
    private Converter converter;

    */
/**
     * Injects dependencies into the servlet.
     *
     * @param workoutService     the service for workout-related operations.
     * @param validationService  the service for validating user input.
     * @param workoutTypeMapper  the mapper for mapping workout types to DTOs.
     * @param converter          the converter for converting objects to JSON.
     *//*

    @Inject
    public void inject(WorkoutService workoutService, ValidationService validationService,
                       WorkoutTypeMapper workoutTypeMapper, Converter converter) {
        this.workoutService = workoutService;
        this.validationService = validationService;
        this.workoutTypeMapper = workoutTypeMapper;
        this.converter = converter;
    }

    */
/**
     * Handles HTTP GET requests to retrieve all workout types for the authenticated user.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     *//*

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        List<WorkoutType> workoutTypes = workoutService.getAllType(authentication.getLogin());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(workoutTypes.stream().map(workoutTypeMapper::toDto).toList()));

    }

    */
/**
     * Handles HTTP POST requests to add a new workout type for the authenticated user.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     *//*

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Authentication authentication = (Authentication) (req.getServletContext()).getAttribute("authentication");
        WorkoutTypeDto workoutTypeDto = converter.getRequestBody(req, WorkoutTypeDto.class);

        List<ValidationError> validationErrors = validationService.validateAndReturnErrors(workoutTypeDto);

        if (validationErrors.isEmpty()) {
            WorkoutType workoutType = workoutService.saveWorkoutType(authentication.getLogin(), workoutTypeDto.getType());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter()
                    .append(converter.convertObjectToJson(workoutTypeMapper.toDto(workoutType)));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter()
                    .append(converter.convertObjectToJson(validationErrors));
        }
    }
}
*/
