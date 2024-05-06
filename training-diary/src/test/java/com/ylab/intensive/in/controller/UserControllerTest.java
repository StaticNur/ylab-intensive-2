package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера пользователей")
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private GeneratorResponseMessage generatorResponseMessage;

    @Mock
    private Authentication authentication;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    @Test
    @DisplayName("Должен успешно отобразить тренировки всех пользователей")
    void shouldViewTrainingsForAllUsers() throws Exception {
        List<User> userList = Collections.singletonList(new User());
        List<User> userWithWorkouts = Collections.singletonList(new User());
        UserDto userDto = new UserDto();
        when(userService.getAllUser()).thenReturn(userList);
        when(workoutService.getAllUsersWorkouts(userList)).thenReturn(userWithWorkouts);
        when(userMapper.toDto(any())).thenReturn(userDto);

        mockMvc.perform(get("/training-diary/users/workouts"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Должен успешно изменить права доступа пользователя")
    void shouldChangeUserRights() throws Exception {
        User user = new User();
        user.setEmail("email");
        when(userService.changeUserPermissions(any(), any())).thenReturn(user);

        mockMvc.perform(patch("/training-diary/users/{uuid}/access", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"newRole\": \"USER\"\n" +
                                 "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Должен возвращать ошибки валидации при изменении прав доступа пользователя")
    void shouldReturnValidationErrorsOnChangeUserRights() throws Exception {
        when(generatorResponseMessage.generateErrorMessage(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(patch("/training-diary/users/{uuid}/access", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"newRole\": \"\"\n" +
                                 "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Должен успешно отобразить аудит действий пользователя")
    void shouldViewAudit() throws Exception {
        AuditDto auditDto = new AuditDto();
        when(userService.getAudit(any(String.class), any())).thenReturn(auditDto);
        when(authentication.getName()).thenReturn("testUser");

        mockMvc.perform(get("/training-diary/user/audit")
                        .param("page", "0")
                        .param("count", "10"))
                .andExpect(status().isOk());
    }
}
