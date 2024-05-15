package com.ylab.intensive.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.tag.IntegrationTest;
import com.ylab.intensive.util.validation.GeneratorResponseMessage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = WorkoutController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@IntegrationTest
@DisplayName("Тест контроллера тренировок")
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutController workoutController;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private WorkoutMapper workoutMapper;

    @MockBean
    private Authentication authentication;

    @MockBean
    private GeneratorResponseMessage generatorResponseMessage;

    @MockBean
    private JwtTokenService jwtTokenService;

    @BeforeEach
    public void setup() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен успешно отобразить статистику тренировок")
        void shouldViewStatistics() throws Exception {
            mockMvc.perform(get("/training-diary/statistics")
                            .param("begin", "2023-01-01")
                            .param("end", "2023-12-31"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Должен успешно добавить дополнительную информацию о тренировке")
        void shouldSaveAdditionalInformation() throws Exception {
            WorkoutInfoDto workoutInfoDto = new WorkoutInfoDto(new HashMap<>());
            workoutInfoDto.getWorkoutInfo().put("title", "new info");

            mockMvc.perform(post("/training-diary/workout-info/{uuid}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(workoutInfoDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Должен успешно отобразить предыдущие тренировки пользователя")
        void shouldViewWorkouts() throws Exception {
            mockMvc.perform(get("/training-diary/workouts"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Должен успешно сохранить новую тренировку пользователя")
        void shouldSaveWorkout() throws Exception {
            WorkoutDto workoutDto = new WorkoutDto(UUID.randomUUID(),
                    "2020-12-22", "cardio", "1:0:0", 1000.0f, Collections.emptyMap());

            mockMvc.perform(post("/training-diary/workouts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(workoutDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Должен успешно редактировать тренировку пользователя")
        void shouldEditWorkout() throws Exception {
            WorkoutInfoDto workoutInfoDto = new WorkoutInfoDto(new HashMap<>());
            workoutInfoDto.getWorkoutInfo().put("title", "new info");

            mockMvc.perform(put("/training-diary/workouts/{uuid}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(workoutInfoDto)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Должен успешно удалить тренировку пользователя")
        void shouldDeleteWorkout() throws Exception {
            mockMvc.perform(delete("/training-diary/workouts/{uuid}", UUID.randomUUID()))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {

        @ParameterizedTest
        @CsvFileSource(resources = {"/csv/saveWorkoutNotValid.csv"}, delimiterString = ";", numLinesToSkip = 1)
        @DisplayName("Должен возвращать ошибки валидации при сохранении новой тренировки пользователя")
        void shouldReturnValidationErrorsOnSaveWorkout(String date,
                                                       String type,
                                                       String durationStr,
                                                       Float calorie) throws Exception {
            WorkoutDto workoutDto = new WorkoutDto(UUID.randomUUID(),
                    date, type, durationStr, calorie, Collections.emptyMap());

            mockMvc.perform(post("/training-diary/workouts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(workoutDto)))
                    .andExpect(status().isBadRequest());

            verify(workoutService, never()).addWorkout(anyString(), any());
        }

        @ParameterizedTest
        @CsvFileSource(resources = {"/csv/editWorkoutNotValid.csv"}, delimiterString = ";", numLinesToSkip = 1)
        @DisplayName("Должен возвращать ошибки валидации при редактировании тренировки пользователя")
        void shouldReturnValidationErrorsOnEditWorkout(String type,
                                                       String durationStr,
                                                       Float calorie) throws Exception {
            EditWorkout workoutDto = new EditWorkout(type, durationStr, calorie, Collections.emptyMap());

            mockMvc.perform(put("/training-diary/workouts/{uuid}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(workoutDto)))
                    .andExpect(status().isBadRequest());

            verify(workoutService, never()).updateWorkout(anyString(), anyString(), any());
        }

        @Test
        @DisplayName("Должен возвращать ошибки валидации при добавлении дополнительной информации о тренировке")
        void shouldReturnValidationErrorsOnSaveAdditionalInformation() throws Exception {
            mockMvc.perform(post("/training-diary/workout-info/{uuid}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("\"workoutInfo\": "))
                    .andExpect(status().isBadRequest());
        }
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}