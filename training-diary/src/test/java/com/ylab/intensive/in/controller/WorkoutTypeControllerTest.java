package com.ylab.intensive.in.controller;

import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.WorkoutTypeDto;
import com.ylab.intensive.model.entity.WorkoutType;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тест контроллера типов тренировок")
class WorkoutTypeControllerTest {

    @InjectMocks
    private WorkoutTypeController workoutTypeController;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private WorkoutTypeMapper workoutTypeMapper;

    @Mock
    private GeneratorResponseMessage generatorResponseMessage;

    @Mock
    private Authentication authentication;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        try (AutoCloseable closeable = MockitoAnnotations.openMocks(this)) {
            mockMvc = MockMvcBuilders.standaloneSetup(workoutTypeController).build();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    @Test
    @DisplayName("Должен успешно отобразить все типы тренировок пользователя")
    void shouldViewTypes() throws Exception {
        String email = "testUser";
        when(authentication.getName()).thenReturn(email);
        when(workoutService.getAllType(email)).thenReturn(Collections.emptyList());
        when(workoutTypeMapper.toDto(new WorkoutType())).thenReturn(new WorkoutTypeDto());

        mockMvc.perform(get("/training-diary/workouts/type"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Должен успешно сохранить новый тип тренировки пользователя")
    void shouldSaveType() throws Exception {
        String email = "testUser";
        WorkoutType workoutType = new WorkoutType();
        when(authentication.getName()).thenReturn(email);
        when(workoutService.saveWorkoutType(any(String.class), any(String.class))).thenReturn(workoutType);
        when(workoutTypeMapper.toDto(workoutType)).thenReturn(new WorkoutTypeDto());

        mockMvc.perform(post("/training-diary/workouts/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"type\": \"cardio\"\n" +
                                 "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Должен возвращать ошибки валидации при сохранении нового типа тренировки пользователя")
    void shouldReturnValidationErrorsOnSaveType() throws Exception {
        when(generatorResponseMessage.generateErrorMessage(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/training-diary/workouts/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                 "  \"type\": \n" +
                                 "}"))
                .andExpect(status().isBadRequest());
    }
}
