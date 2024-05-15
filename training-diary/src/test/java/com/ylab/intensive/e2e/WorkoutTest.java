package com.ylab.intensive.e2e;

import com.ylab.intensive.e2e.config.E2ETestConfig;
import com.ylab.intensive.e2e.config.HeaderUtils;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.tag.E2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@E2ETest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = E2ETestConfig.class)
@DisplayName("Проверка тренировки")
class WorkoutTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;

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
    }
}
