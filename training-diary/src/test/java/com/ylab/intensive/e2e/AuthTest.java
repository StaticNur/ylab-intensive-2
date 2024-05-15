package com.ylab.intensive.e2e;

import com.ylab.intensive.e2e.config.E2ETestConfig;
import com.ylab.intensive.e2e.config.HeaderUtils;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.tag.E2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
@DisplayName("Проверка авторизации")
class AuthTest {

    @Autowired
    private HeaderUtils headerUtils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Успешная авторизация пользователя")
        void testAuthenticationUser_shouldReturnStatusCreatedAndBodyWithCreatedUser_whenAuthenticationValid() {
            LoginDto loginDto = new LoginDto("user@example.com", "psw2");

            RequestEntity<LoginDto> request = RequestEntity
                    .post("/training-diary/auth/login")
                    .headers(headerUtils.withAdminToken())
                    .body(loginDto);

            ResponseEntity<JwtResponse> response = restTemplate
                    .exchange(request, JwtResponse.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertThat(response.getBody())
                    .hasFieldOrPropertyWithValue("login", "user@example.com")
                    .hasNoNullFieldsOrProperties();
        }
    }

    @Nested
    @DisplayName("Bad request")
    class BadRequest {
        @Test
        @DisplayName("При некорректном email должен возвращать статус код BAD REQUEST")
        void testCreationUser_shouldReturnStatusBadRequest_whenEmailNotValid() {
            RegistrationDto registrationDto = new RegistrationDto("1234567", "", Role.ADMIN);

            RequestEntity<RegistrationDto> request = RequestEntity
                    .post("/training-diary/auth/registration")
                    .headers(headerUtils.withAdminToken())
                    .body(registrationDto);

            ResponseEntity<List<CustomFieldError>> response = restTemplate
                    .exchange(request, new ParameterizedTypeReference<>() {
                    });

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertThat(response.getBody())
                    .size().isGreaterThanOrEqualTo(1);
        }
    }
}
