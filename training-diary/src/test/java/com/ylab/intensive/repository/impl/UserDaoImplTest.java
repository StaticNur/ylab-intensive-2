package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.repository.UserDao;
import com.ylab.intensive.repository.config.TestRepositories;
import com.ylab.intensive.tag.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тесты для реализации UserDao")
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен сохранять пользователя в базе данных")
        void shouldSaveUser() {
            User user = new User();
            user.setUuid(UUID.randomUUID());
            user.setEmail("123test345@34example43.com");
            user.setPassword("password");
            int roleId = 1;

            User savedUser = userDao.save(user, roleId);

            assertThat(savedUser.getId()).isPositive();
            assertThat(savedUser.getEmail()).isEqualTo("123test345@34example43.com");
            assertThat(savedUser.getPassword()).isEqualTo("password");
        }

        @Test
        @DisplayName("Должен находить пользователя по электронной почте")
        void shouldFindUserByEmail() {
            String email = "admin@example.com";

            assertThat(userDao.findByEmail(email))
                    .isPresent()
                    .hasValueSatisfying(user -> assertThat(user.getEmail()).isEqualTo(email));
        }

        @Test
        @DisplayName("Должен находить пользователя по UUID")
        void shouldFindUserByUUID() {
            UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

            assertThat(userDao.findByUUID(uuid))
                    .isPresent()
                    .hasValueSatisfying(user -> assertThat(user.getUuid()).isEqualTo(uuid));
        }

        @Test
        @DisplayName("Должен обновлять роль пользователя")
        void shouldUpdateUserRole() {
            UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            int newRoleId = 2;

            assertThat(userDao.updateUserRole(uuid, newRoleId))
                    .isTrue();
        }

        @Test
        @DisplayName("Должен возвращать список всех пользователей")
        void shouldFindAllUsers() {
            List<User> users = userDao.findAll();

            assertThat(users)
                    .isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Не должен находить пользователя по электронной почте")
        void notShouldFindUserByEmail() {
            String email = "test.com";

            Optional<User> userOptional = userDao.findByEmail(email);

            assertThat(userOptional)
                    .isNotPresent();
        }
    }
}