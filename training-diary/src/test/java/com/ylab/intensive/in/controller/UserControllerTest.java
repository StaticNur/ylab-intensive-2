package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.tag.IntegrationTest;
import com.ylab.intensive.util.MetricService;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@IntegrationTest
@DisplayName("Тест контроллера пользователей")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private MetricService metricService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private GeneratorResponseMessage generatorResponseMessage;

    @MockBean
    private Authentication authentication;

    @BeforeEach
    public void setup() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен успешно отобразить тренировки всех пользователей")
        void shouldViewTrainingsForAllUsers() throws Exception {
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

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Должен возвращать ошибки валидации при изменении прав доступа пользователя")
        void shouldReturnValidationErrorsOnChangeUserRights() throws Exception {
            mockMvc.perform(patch("/training-diary/users/{uuid}/access", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                     "  \"newRole\": \"\"\n" +
                                     "}"))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).changeUserPermissions(anyString(), any());
        }
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}