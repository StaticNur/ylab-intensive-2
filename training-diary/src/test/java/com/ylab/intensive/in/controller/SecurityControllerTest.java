package com.ylab.intensive.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.tag.IntegrationTest;
import com.ylab.intensive.util.MetricService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SecurityController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@IntegrationTest
@DisplayName("Тест контроллера регистрации и авторизации")
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityController securityController;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private MetricService metricService;

    @MockBean
    private GeneratorResponseMessage generatorResponseMessage;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен успешно зарегистрировать нового пользователя")
        void shouldRegisterNewUser() throws Exception {
            RegistrationDto registrationDto = new RegistrationDto("acom", "psw1", Role.ADMIN);
            User user = new User();
            user.setEmail("acom");
            when(userService.registerUser(registrationDto)).thenReturn(user);

            mockMvc.perform(post("/training-diary/auth/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(registrationDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Должен успешно авторизовать пользователя")
        void shouldAuthorizeUser() throws Exception {
            LoginDto loginDto = new LoginDto("token", "token");
            JwtResponse jwtResponse = new JwtResponse("token","","");
            when(userService.login(loginDto)).thenReturn(jwtResponse);

            mockMvc.perform(post("/training-diary/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(loginDto)))
                    .andExpect(status().isOk());
        }
    }


    @Nested
    @DisplayName("Negative testing")
    class Negative {

        @ParameterizedTest
        @CsvFileSource(resources = {"/csv/authorizationNotValid.csv"}, delimiterString = ";", numLinesToSkip = 1)
        @DisplayName("Должен возвращать ошибки валидации при авторизации пользователя")
        void shouldReturnValidationErrorsOnAuthorization(String email,
                                                         String password) throws Exception {
            LoginDto loginDto = new LoginDto(email, password);

            mockMvc.perform(post("/training-diary/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(loginDto)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).login(loginDto);
        }

        @ParameterizedTest
        @CsvFileSource(resources = {"/csv/registrationNotValid.csv"}, delimiterString = ";", numLinesToSkip = 1)
        @DisplayName("Должен возвращать ошибки валидации при регистрации нового пользователя")
        void shouldReturnValidationErrorsOnRegistration(String email,
                                                        String password,
                                                        String roleStr) throws Exception {
            RegistrationDto registrationDto = new RegistrationDto(email, password, Role.valueOf(roleStr));

            mockMvc.perform(post("/training-diary/auth/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(registrationDto)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).registerUser(registrationDto);
        }

        @Test
        @DisplayName("Должен возвращать ошибки валидации при продлении срока службы токена.")
        void shouldSuccessfullyExtendTheLifeOfTheToken() throws Exception {
            RefreshTokenDto tokenDto = new RefreshTokenDto("");

            mockMvc.perform(post("/training-diary/auth/refresh-token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(tokenDto)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).updateToken(anyString());
        }
    }
}