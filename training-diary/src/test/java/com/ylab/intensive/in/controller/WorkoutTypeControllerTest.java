package com.ylab.intensive.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.mapper.WorkoutTypeMapper;
import com.ylab.intensive.model.dto.WorkoutTypeDto;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.tag.IntegrationTest;
import com.ylab.intensive.util.MetricService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = WorkoutTypeController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@IntegrationTest
@DisplayName("Тест контроллера типов тренировок")
class WorkoutTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutTypeController workoutTypeController;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private WorkoutTypeMapper workoutTypeMapper;

    @MockBean
    private GeneratorResponseMessage generatorResponseMessage;

    @MockBean
    private Authentication authentication;

    @MockBean
    private MetricService metricService;

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
        @DisplayName("Должен успешно отобразить все типы тренировок пользователя")
        void shouldViewTypes() throws Exception {
            mockMvc.perform(get("/training-diary/workouts/type"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Должен успешно сохранить новый тип тренировки пользователя")
        void shouldSaveType() throws Exception {
            mockMvc.perform(post("/training-diary/workouts/type")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                     "  \"type\": \"cardio\"\n" +
                                     "}"))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @ParameterizedTest
        @CsvFileSource(resources = {"/csv/saveTypeNotValid.csv"}, delimiterString = ";", numLinesToSkip = 1)
        @DisplayName("Должен возвращать ошибки валидации при сохранении нового типа тренировки пользователя")
        void shouldReturnValidationErrorsOnSaveType(String type) throws Exception {
            WorkoutTypeDto workoutTypeDto = new WorkoutTypeDto(type);

            mockMvc.perform(post("/training-diary/workouts/type")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(workoutTypeDto)))
                    .andExpect(status().isBadRequest());

            verify(workoutService, never()).saveWorkoutType(anyString(), anyString());
        }
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}