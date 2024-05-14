package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.repository.AuditDao;
import com.ylab.intensive.repository.container.PostgresTestContainer;
import com.ylab.intensive.repository.container.TestConfigurationEnvironment;
import com.ylab.intensive.repository.extractor.AuditExtractor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("audit dao implementation test")
public class AuditDaoImplTest extends TestConfigurationEnvironment {

    private static AuditDao auditDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        jdbcTemplate = PostgresTestContainer.getJdbcTemplate();
        auditDao = new AuditDaoImpl(jdbcTemplate, new AuditExtractor());
    }

    @Test
    @DisplayName("Должен извлекать действия пользователя с пагинацией")
    void shouldGetUserActionsWithPagination() {
        int userId = 1;
        Pageable pageable = new Pageable(0, 10);

        List<Audit> userActions = auditDao.getUserActions(userId, pageable);

        assertThat(userActions).hasSize(2);
        assertThat(userActions.get(0).getAction()).isEqualTo("Login");
    }

    @Test
    @DisplayName("Должен добавлять новое действие пользователя в таблицу audit")
    void shouldInsertUserAction() {
        int userId = 1;
        String action = "Пользователь сохранил новую тренировку.";

        auditDao.insertUserAction(userId, action);

        List<Audit> auditList = auditDao.getUserActions(userId, new Pageable(0, 10));
        assertThat(auditList)
                .extracting(Audit::getAction)
                .contains(action);
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}






