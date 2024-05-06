package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.WorkoutMapper;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.Workout;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера тренировок")
class WorkoutControllerTest {

    @InjectMocks
    private WorkoutController workoutController;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private GeneratorResponseMessage generatorResponseMessage;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(workoutController).build();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    @Test
    @DisplayName("Должен успешно отобразить статистику тренировок")
    void shouldViewStatistics() throws Exception {
        when(workoutService.getWorkoutStatistics(any(String.class), any(String.class), any(String.class)))
                .thenReturn(new StatisticsDto(1000));

        mockMvc.perform(get("/training-diary/statistics")
                        .param("begin", "2023-01-01")
                        .param("end", "2023-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Должен успешно добавить дополнительную информацию о тренировке")
    void shouldSaveAdditionalInformation() throws Exception {
        UUID uuid = UUID.randomUUID();
        Workout workout = new Workout();
        when(workoutService.addWorkoutInfo(any(String.class), any(String.class), any(WorkoutInfoDto.class)))
                .thenReturn(workout);
        when(workoutMapper.toDto(workout)).thenReturn(new WorkoutDto());

        mockMvc.perform(post("/training-diary/workout-info/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"workoutInfo\": {\n" +
                                 "    \"title\": \"info\"\n" +
                                 "  }\n" +
                                 "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Должен возвращать ошибки валидации при добавлении дополнительной информации о тренировке")
    void shouldReturnValidationErrorsOnSaveAdditionalInformation() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(generatorResponseMessage.generateErrorMessage(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/training-diary/workout-info/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"workoutInfo\": "))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Должен успешно отобразить предыдущие тренировки пользователя")
    void shouldViewWorkouts() throws Exception {
        when(authentication.getName()).thenReturn("testUser");
        when(workoutService.getAllWorkoutsByUser("testUser")).thenReturn(Collections.emptyList());
        when(workoutMapper.toDto(any())).thenReturn(new WorkoutDto());

        mockMvc.perform(get("/training-diary/workouts"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Должен успешно сохранить новую тренировку пользователя")
    void shouldSaveWorkout() throws Exception {
        when(authentication.getName()).thenReturn("testUser");
        when(workoutService.addWorkout(any(String.class), any(WorkoutDto.class))).thenReturn(new WorkoutDto());

        mockMvc.perform(post("/training-diary/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"date\": \"2020-12-22\",\n" +
                                 "  \"type\": \"cardio\",\n" +
                                 "  \"duration\": \"1:0:0\",\n" +
                                 "  \"calorie\": 1000.0,\n" +
                                 "  \"workoutInfo\": {}\n" +
                                 "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Должен возвращать ошибки валидации при сохранении новой тренировки пользователя")
    void shouldReturnValidationErrorsOnSaveWorkout() throws Exception {
        when(generatorResponseMessage.generateErrorMessage(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/training-diary/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"date\": ,\n" +
                                 "  \"type\": ,\n" +
                                 "  \"duration\": \"1:0:0\",\n" +
                                 "  \"calorie\": -1000.0,\n" +
                                 "  \"workoutInfo\": {}\n" +
                                 "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Должен успешно редактировать тренировку пользователя")
    void shouldEditWorkout() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(authentication.getName()).thenReturn("testUser");
        when(workoutService.updateWorkout(any(String.class), any(String.class), any(EditWorkout.class)))
                .thenReturn(new Workout());
        when(workoutMapper.toDto(any())).thenReturn(new WorkoutDto());

        mockMvc.perform(put("/training-diary/workouts/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"workoutInfo\": {\n" +
                                 "    \"title\": \"new info\"\n" +
                                 "  }\n" +
                                 "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Должен возвращать ошибки валидации при редактировании тренировки пользователя")
    void shouldReturnValidationErrorsOnEditWorkout() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(generatorResponseMessage.generateErrorMessage(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(put("/training-diary/workouts/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"calorie\": " +
                                 "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Должен успешно удалить тренировку пользователя")
    void shouldDeleteWorkout() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(authentication.getName()).thenReturn("testUser");
        doNothing().when(workoutService).deleteWorkout(any(String.class), any(String.class));

        mockMvc.perform(delete("/training-diary/workouts/{uuid}", uuid))
                .andExpect(status().isOk());
    }
}
