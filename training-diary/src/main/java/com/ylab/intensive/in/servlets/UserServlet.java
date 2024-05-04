/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.AllowedRoles;
import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.UserService;
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

*/
/**
 * Servlet for handling user-related operations.
 * <p>
 * This servlet handles HTTP GET requests to retrieve user information and their associated workouts.
 * It also handles HTTP PATCH requests to modify user access rights.
 * </p>
 *
 * @since 1.0
 *//*

@WebServlet("/training-diary/users/*")
@ApplicationScoped
@NoArgsConstructor
public class UserServlet extends HttpServlet {
    private UserService userService;
    private ValidationService validationService;
    private WorkoutService workoutService;
    private Converter converter;
    private UserMapper userMapper;

    */
/**
     * Injects dependencies into the servlet.
     *
     * @param userService       the service for user-related operations.
     * @param validationService the service for validating user input.
     * @param workoutService    the service for workout-related operations.
     * @param converter         the converter for converting objects to JSON.
     * @param userMapper        the mapper for mapping user entities to DTOs.
     *//*

    @Inject
    public void inject(UserService userService, ValidationService validationService,
                       WorkoutService workoutService, Converter converter,
                       UserMapper userMapper) {
        this.userService = userService;
        this.validationService = validationService;
        this.workoutService = workoutService;
        this.converter = converter;
        this.userMapper = userMapper;
    }

    */
/**
     * Handles HTTP PATCH requests.
     *
     * @param request  the HTTP servlet request.
     * @param response the HTTP servlet response.
     * @throws IOException      if an I/O error occurs while handling the request.
     * @throws ServletException if a servlet exception occurs while handling the request.
     *//*

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String httpMethod = request.getMethod();

        if ("PATCH".equalsIgnoreCase(httpMethod)) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    */
/**
     * Handles HTTP GET requests.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     *//*

    @Override
    @AllowedRoles({Role.ADMIN})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathEnd = req.getPathInfo();

        if ("/workouts".equals(pathEnd)) {
            List<User> userList = userService.getAllUser();
            List<User> userWithWorkouts = workoutService.getAllUsersWorkouts(userList);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter()
                    .append(converter.convertObjectToJson(userWithWorkouts.stream().map(userMapper::toDto).toList()));
        } else {
            sendErrorMessage(resp, new ExceptionResponse("Not found endpoint."));
        }
    }

    */
/**
     * Handles HTTP PATCH requests to modify user access rights.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     *//*

    @AllowedRoles({Role.ADMIN})
    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuidAndAccess = req.getPathInfo();
        String[] pathParts = uuidAndAccess.split("/");
        Pattern pattern = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

        if (pathParts.length > 2 && pattern.matcher(pathParts[pathParts.length - 2]).matches()
            && "access".equals(pathParts[pathParts.length - 1])) {

            ChangeUserRightsDto changeUserRightsDto = converter.getRequestBody(req, ChangeUserRightsDto.class);
            List<ValidationError> validationErrors = validationService.validateAndReturnErrors(changeUserRightsDto);

            if (validationErrors.isEmpty()) {
                User user = userService.changeUserPermissions(pathParts[pathParts.length - 2], changeUserRightsDto);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter()
                        .append(converter
                                .convertObjectToJson(new SuccessResponse("Права доступа успешно изменена! " +
                                                                         "Теперь для данного пользователя " + user.getEmail()
                                                                         + " требуется повторная авторизация для обновления токена.")));
            } else {
                sendErrorMessage(resp, validationErrors);
            }
        } else {
            sendErrorMessage(resp, new ExceptionResponse("Not found endpoint. Maybe the 'uuid' is not correct"));
        }
    }

    */
/**
     * Sends error message as response.
     *
     * @param resp   the HTTP servlet response.
     * @param object the object representing the error response.
     * @throws IOException if an I/O error occurs while sending the response.
     *//*

    private void sendErrorMessage(HttpServletResponse resp, Object object) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
                .append(converter.convertObjectToJson(object));
    }
}
*/
