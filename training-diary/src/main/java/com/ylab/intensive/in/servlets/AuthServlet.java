/*
package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.ValidationService;
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
 * Servlet for handling user authentication-related requests.
 * <p>
 * This servlet handles registration and login requests. It validates incoming data,
 * performs registration or login operations accordingly, and sends appropriate responses.
 * </p>
 *
 * @since 1.0
 *//*

@WebServlet("/training-diary/auth/*")
@NoArgsConstructor
@ApplicationScoped
public class AuthServlet extends HttpServlet {
    */
/**
     * The service for user-related operations.
     *//*

    private UserService userService;

    */
/**
     * The service for validating user input.
     *//*

    private ValidationService validationService;

    */
/**
     * The mapper for mapping user entities to DTOs.
     *//*

    private UserMapper userMapper;

    */
/**
     * The converter for converting objects to JSON and vice versa.
     *//*

    private Converter converter;

    */
/**
     * Injects dependencies into the servlet.
     *
     * @param userService      the user service for user-related operations.
     * @param validationService the validation service for validating user input.
     * @param userMapper       the mapper for mapping user entities to DTOs.
     * @param converter        the converter for converting objects to JSON and vice versa.
     *//*

    @Inject
    public void inject(UserService userService, ValidationService validationService,
                       UserMapper userMapper, Converter converter) {
        this.userService = userService;
        this.validationService = validationService;
        this.userMapper = userMapper;
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
        String path = req.getPathInfo();

        if ("/registration".equals(path)) {
            register(req, resp);
        } else if ("/login".equals(path)) {
            login(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter()
                    .append(converter.convertObjectToJson(new ExceptionResponse("Not found endpoint.")));
        }
    }

    */
/**
     * Handles user registration.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     *//*

    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RegistrationDto registrationDto = converter.getRequestBody(req, RegistrationDto.class);
        List<ValidationError> validationErrors = validationService.validateAndReturnErrors(registrationDto);

        if (validationErrors.isEmpty()) {
            User user = userService.registerUser(registrationDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter()
                    .append(converter.convertObjectToJson(userMapper.toDto(user)));
        } else {
            sendErrorMessage(resp, validationErrors);
        }
    }

    */
/**
     * Handles user login.
     *
     * @param req  the HTTP servlet request.
     * @param resp the HTTP servlet response.
     * @throws IOException if an I/O error occurs while handling the request.
     *//*

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoginDto loginDto = converter.getRequestBody(req, LoginDto.class);
        List<ValidationError> validationErrors = validationService.validateAndReturnErrors(loginDto);

        if (validationErrors.isEmpty()) {
            JwtResponse response = userService.login(loginDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter()
                    .append(converter.convertObjectToJson(response));
        } else {
            sendErrorMessage(resp, validationErrors);
        }
    }

    */
/**
     * Sends error message as response.
     *
     * @param resp             the HTTP servlet response.
     * @param validationErrors the list of validation errors.
     * @throws IOException if an I/O error occurs while sending the response.
     *//*

    private void sendErrorMessage(HttpServletResponse resp, List<ValidationError> validationErrors) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
                .append(converter.convertObjectToJson(validationErrors));
    }
}
*/
