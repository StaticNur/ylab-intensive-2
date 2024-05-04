/*
package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.container.PostgresTestContainer;
import com.ylab.intensive.dao.container.TestConfigurationEnvironment;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Database Operations Testing")
class UserDaoImplTest extends TestConfigurationEnvironment {

    private static UserDaoImpl userDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        userDao = new UserDaoImpl();
    }

    @Test
    @DisplayName("Save user successfully")
    void testSave() {
        User user = new User();
        user.setUuid(UUID.fromString("622e0957-e81b-19d3-a446-426614879357"));
        user.setEmail("test@email.com");
        user.setPassword("password");
        int roleId = Role.USER.getValue();

        User userSaved = userDao.save(user, roleId);

        assertThat(userSaved.getId() != 0).isTrue();
    }

    @Test
    @DisplayName("Test findByEmail method - user exists")
    void testFindByEmail() {

        assertThat(userDao.findByEmail("admin@example.com"))
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("role", Role.ADMIN);
    }

    @Test
    @DisplayName("Find workout by date - user does not exist")
    void testFindByEmail_UserDoesNotExist() {
        assertThat(userDao.findByEmail("dw3rwed@example.com")).isEmpty();
    }

    @Test
    @DisplayName("Update user role - success")
    void testUpdateUserRole_Success() {
        String email = "test23@email.com";
        int roleId = Role.ADMIN.getValue();
        User user = new User();
        user.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174012"));
        user.setEmail(email);
        user.setPassword("password");

        User userSaved = userDao.save(user, Role.USER.getValue());

        boolean result = userDao.updateUserRole(userSaved.getUuid(), roleId);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Update user role - user does not exist")
    void testUpdateUserRole_UserDoesNotExist() {
        boolean result = userDao.updateUserRole(UUID.fromString("123e4567-e89b-12d3-a456-426614174029"),
                Role.ADMIN.getValue());
        assertThat(result).isFalse();
    }

    @AfterAll
    static void destroy() {
        postgreSQLContainer.stop();
    }
}
*/
