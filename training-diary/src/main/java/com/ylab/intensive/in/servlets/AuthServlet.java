package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
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

@WebServlet("/training-diary/auth/*")
@NoArgsConstructor
@ApplicationScoped
public class AuthServlet extends HttpServlet {
    private UserService userService;
    private ValidationService validationService;
    private UserMapper userMapper;
    private Converter converter;

    @Inject
    public void inject(UserService userService, ValidationService validationService,
                       UserMapper userMapper, Converter converter) {
        this.userService = userService;
        this.validationService = validationService;
        this.userMapper = userMapper;
        this.converter = converter;
    }

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

    @Auditable
    @Loggable
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

    @Auditable
    @Loggable
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

    private void sendErrorMessage(HttpServletResponse resp, List<ValidationError> validationErrors) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter()
                .append(converter.convertObjectToJson(validationErrors));
    }
}
