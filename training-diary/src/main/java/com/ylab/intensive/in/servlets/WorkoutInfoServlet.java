/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.dto.WorkoutInfoDto;
import com.ylab.intensive.model.entity.Workout;
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

*/
/**
 * Servlet for handling workout information-related operations.
 * <p>
 * This servlet handles HTTP POST requests to add workout information for a specific workout identified by its UUID.
 * It injects dependencies such as the workout service, workout mapper, and converter for processing the requests.
 * </p>
 *
 * @since 1.0
 *//*

@WebServlet("/training-diary/workout-info/*")
@ApplicationScoped
@NoArgsConstructor
public class WorkoutInfoServlet extends HttpServlet {
    private WorkoutService workoutService;
    private WorkoutMapper workoutMapper;
    private Converter converter;

    */
/**
     * Injects dependencies into the servlet.
     *
     * @param workoutService the service for workout-related operations.
     * @param workoutMapper  the mapper for mapping workout entities to DTOs.
     * @param converter      the converter for converting objects to JSON.
     *//*

    @Inject
    public void inject(WorkoutService workoutService, WorkoutMapper workoutMapper,
                       Converter converter) {
        this.workoutService = workoutService;
        this.workoutMapper = workoutMapper;
        this.converter = converter;
    }

    */
/**
     * Handles HTTP POST requests.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     *//*

    @Override
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

    */
/**
     * Sends error message as response when UUID is not correct.
     *
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while sending the response.
     *//*

    private void sendErrorMessage(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
                .append(converter.convertObjectToJson(new ExceptionResponse("Not found endpoint."
                                                                            + " Maybe the 'uuid' is not correct")));
    }
}
*/
