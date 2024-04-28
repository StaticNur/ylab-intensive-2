/*
package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.container.PostgresTestContainer;
import com.ylab.intensive.dao.container.TestConfigurationEnvironment;
import com.ylab.intensive.exception.DaoException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Audit Database Operations Testing")
class AuditMapperDaoImplTest extends TestConfigurationEnvironment {

    private static AuditDaoImpl auditDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        auditDao = new AuditDaoImpl();
    }

    @Test
    @DisplayName("Get user actions - user exists")
    void testGetUserActions_UserExists() {
        int userId = 1;

        List<String> actions = auditDao.getUserActions(userId);

        assertThat(actions).isNotEmpty();
    }

    @Test
    @DisplayName("Get user actions - user does not exist")
    void testGetUserActions_UserDoesNotExist() {
        int userId = 9999;

        List<String> actions = auditDao.getUserActions(userId);

        assertThat(actions).isEmpty();
    }

    @Test
    @DisplayName("Insert user action - success")
    void testInsertUserAction_Success() {
        int userId = 1;
        String action = "Test Action";

        auditDao.insertUserAction(userId, action);

        List<String> actions = auditDao.getUserActions(userId);
        assertThat(actions).contains(action);
    }

    @Test
    @DisplayName("Insert user action - invalid user ID")
    void testInsertUserAction_InvalidUserId() {
        int userId = 9999;
        String action = "Test Action";

        assertThatThrownBy(() -> auditDao.insertUserAction(userId, action))
                .isInstanceOf(DaoException.class);
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
*/
