package com.ylab.intensive.e2e;

import com.ylab.intensive.e2e.config.E2ETestConfig;
import com.ylab.intensive.e2e.config.HeaderUtils;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.repository.WorkoutDao;
import com.ylab.intensive.tag.E2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@E2ETest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = E2ETestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Проверка тренировки")
class WorkoutTest {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @SpyBean
    @Autowired
    private WorkoutDao workoutDao;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Успешно просмотреть статистику тренировки")
        void testViewStatistic() {
            RequestEntity<?> request = RequestEntity
                    .get("/training-diary/statistics")
                    .headers(headerUtils.withUserToken())
                    .build();

            ResponseEntity<StatisticsDto> response = restTemplate
                    .exchange(request, StatisticsDto.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Успешно просмотреть историю тренировок + Кэш")
        void testViewHistoryWorkout_Cache() {
            RequestEntity<?> request = RequestEntity
                    .get("/training-diary/workouts")
                    .headers(headerUtils.withUserToken())
                    .build();

            ResponseEntity<List<WorkoutDto>> response1 = restTemplate
                    .exchange(request, new ParameterizedTypeReference<>() {});
            ResponseEntity<List<WorkoutDto>> response2 = restTemplate
                    .exchange(request, new ParameterizedTypeReference<>() {});
            ResponseEntity<List<WorkoutDto>> response3 = restTemplate
                    .exchange(request, new ParameterizedTypeReference<>() {});

            assertEquals(HttpStatus.OK, response1.getStatusCode());
            assertEquals(HttpStatus.OK, response2.getStatusCode());
            assertEquals(HttpStatus.OK, response3.getStatusCode());
            verify(workoutDao, times(1)).findByUserId(anyInt());
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Инвалидация кеша")
        void testViewHistoryWorkout_NotCached() {
            Random random = new Random();
            EditWorkout editWorkout = new EditWorkout();
            editWorkout.setCalorie(random.nextFloat());

            RequestEntity<?> requestView = RequestEntity
                    .get("/training-diary/workouts")
                    .headers(headerUtils.withUserToken())
                    .build();
            RequestEntity<?> requestEdit = RequestEntity
                    .put("/training-diary/workouts/123e4567-e89b-12d3-a456-426614174003")
                    .headers(headerUtils.withUserToken())
                    .body(editWorkout);

            ResponseEntity<List<WorkoutDto>> response1 = restTemplate
                    .exchange(requestView, new ParameterizedTypeReference<>() {});

            ResponseEntity<WorkoutDto> response11 = restTemplate
                    .exchange(requestEdit, WorkoutDto.class);

            ResponseEntity<List<WorkoutDto>> response2 = restTemplate
                    .exchange(requestView, new ParameterizedTypeReference<>() {});

            ResponseEntity<WorkoutDto> response22 = restTemplate
                    .exchange(requestEdit, WorkoutDto.class);

            ResponseEntity<List<WorkoutDto>> response3 = restTemplate
                    .exchange(requestView, new ParameterizedTypeReference<>() {});

            applicationContext.getBean("cacheManager");
            assertEquals(HttpStatus.OK, response1.getStatusCode());
            assertEquals(HttpStatus.OK, response2.getStatusCode());
            assertEquals(HttpStatus.OK, response3.getStatusCode());
            verify(workoutDao, times(3)).findByUserId(anyInt());
        }
    }

    /**
     * Пример того как можно получить экземпляр бина cacheManager для анализа
     */
    private void cacheManagerBean() {
        applicationContext.getBean("cacheManager");
    }
}
