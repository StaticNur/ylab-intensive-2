package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тест контроллера регистрации и авторизации")
class SecurityControllerTest {

    @InjectMocks
    private SecurityController securityController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private GeneratorResponseMessage generatorResponseMessage;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();
        }
    }

    @Test
    @DisplayName("Должен успешно зарегистрировать нового пользователя")
    void shouldRegisterNewUser() throws Exception {
        User user = new User();
        when(userService.registerUser(any())).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        mockMvc.perform(post("/training-diary/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"email\": \"tes@example.com\",\n" +
                                 "  \"password\": \"psw\",\n" +
                                 "  \"role\": \"ADMIN\"\n" +
                                 "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Должен возвращать ошибки валидации при регистрации нового пользователя")
    void shouldReturnValidationErrorsOnRegistration() throws Exception {
        when(generatorResponseMessage.generateErrorMessage(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/training-diary/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"password\": \"8\",\n" +
                                 "  \"role\": \"AN\"\n" +
                                 "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Должен успешно авторизовать пользователя")
    void shouldAuthorizeUser() throws Exception {
        when(userService.login(any())).thenReturn(new JwtResponse("login", "token", "token"));

        mockMvc.perform(post("/training-diary/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"email\": \"test@example.com\",\n" +
                                 "  \"password\": \"psw\"\n" +
                                 "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Должен возвращать ошибки валидации при авторизации пользователя")
    void shouldReturnValidationErrorsOnAuthorization() throws Exception {
        when(generatorResponseMessage.generateErrorMessage(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/training-diary/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"email\": \"test@example.com\",\n" +
                                 "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Должен успешно продлить срок службы токена.")
    void shouldSuccessfullyExtendTheLifeOfTheToken() throws Exception {
        when(userService.updateToken(any())).thenReturn(new JwtResponse("login", "token", "token"));

        mockMvc.perform(post("/training-diary/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"refreshToken\": \"token\",\n" +
                                 "}"))
                .andExpect(status().isBadRequest());
    }
}

