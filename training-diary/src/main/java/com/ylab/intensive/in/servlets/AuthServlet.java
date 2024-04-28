package com.ylab.intensive.in.servlets;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.ExceptionResponse;
import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.dto.LoginDto;
import com.ylab.intensive.model.dto.RegistrationDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;

@WebServlet("/training-diary/auth/*")
@NoArgsConstructor
@ApplicationScoped
public class AuthServlet extends HttpServlet {
    private UserService userService;
    private UserMapper userMapper;
    private Converter converter;

    @Inject
    public void inject(UserService userService, UserMapper userMapper, Converter converter) {
        this.userService = userService;
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
        User user = userService.registerUser(registrationDto);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(userMapper.toDto(user)));
    }

    @Auditable
    @Loggable
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoginDto loginDto = converter.getRequestBody(req, LoginDto.class);
        JwtResponse response = userService.login(loginDto);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter()
                .append(converter.convertObjectToJson(response));
    }
}
