package com.ylab.intensive.e2e;

import com.ylab.intensive.e2e.config.E2ETestConfig;
import com.ylab.intensive.e2e.config.HeaderUtils;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.tag.E2ETest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@E2ETest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = E2ETestConfig.class)
@DisplayName("Проверка пользователей")
class UserTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен успешно отобразить тренировки всех пользователей")
        void testGetHistoryTrainingAllUsers() {
            RequestEntity<?> request = RequestEntity
                    .get("/training-diary/users/workouts")
                    .headers(headerUtils.withAdminToken())
                    .build();

            ResponseEntity<List<UserDto>> response = restTemplate
                    .exchange(request, new ParameterizedTypeReference<>() {
                    });

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                    .size().isGreaterThanOrEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Security")
    class Secured {
        @Test
        @DisplayName("Должен возвращать статус код Forbidden")
        void testGetHistoryTrainingAllUsers_shouldReturnStatusForbidden_whenUserWithRoleUser() {
            RequestEntity<?> request = RequestEntity
                    .get("/training-diary/users/workouts")
                    .headers(headerUtils.withUserToken())
                    .build();

            ResponseEntity<String> response = restTemplate
                    .exchange(request, String.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }
}