package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.repository.UserDao;
import com.ylab.intensive.repository.container.PostgresTestContainer;
import com.ylab.intensive.repository.container.TestConfigurationEnvironment;
import com.ylab.intensive.repository.extractor.UserExtractor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты для реализации UserDaoImpl")
public class UserDaoImplTest extends TestConfigurationEnvironment {

    private static UserDao userDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        JdbcTemplate jdbcTemplate = PostgresTestContainer.getJdbcTemplate();
        userDao = new UserDaoImpl(jdbcTemplate, new UserExtractor());
    }

    @Test
    @DisplayName("Должен сохранять пользователя в базе данных")
    void shouldSaveUser() {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPassword("password");
        int roleId = 1;

        User savedUser = userDao.save(user, roleId);

        assertThat(savedUser.getId()).isPositive();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("password");
    }

    @Test
    @DisplayName("Должен находить пользователя по электронной почте")
    void shouldFindUserByEmail() {
        String email = "admin@example.com";

        Optional<User> userOptional = userDao.findByEmail(email);

        assertThat(userOptional).isPresent();
        User user = userOptional.get();
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Не должен находить пользователя по электронной почте")
    void notShouldFindUserByEmail() {
        String email = "test.com";

        Optional<User> userOptional = userDao.findByEmail(email);

        assertThat(userOptional).isNotPresent();
    }

    @Test
    @DisplayName("Должен находить пользователя по UUID")
    void shouldFindUserByUUID() {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Optional<User> userOptional = userDao.findByUUID(uuid);

        assertThat(userOptional).isPresent();
        User user = userOptional.get();
        assertThat(user.getUuid()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Должен обновлять роль пользователя")
    void shouldUpdateUserRole() {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        int newRoleId = 2;

        boolean updated = userDao.updateUserRole(uuid, newRoleId);

        assertThat(updated).isTrue();
    }

    @Test
    @DisplayName("Должен возвращать список всех пользователей")
    void shouldFindAllUsers() {
        List<User> users = userDao.findAll();

        assertThat(users).isNotEmpty();
    }
    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}

