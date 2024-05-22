package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.repository.AuditDao;
import com.ylab.intensive.repository.config.TestRepositories;
import com.ylab.intensive.tag.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("audit dao implementation test")
class AuditDaoImplTest {

    @Autowired
    private AuditDao auditDao;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен извлекать действия пользователя с пагинацией")
        void shouldGetUserActionsWithPagination() {
            int userId = 1;
            Pageable pageable = new Pageable(0, 10);

            assertThat(auditDao.getUserActions(userId, pageable))
                    .anyMatch(ua -> ua.getAction().equals("Login"))
                    .size().isGreaterThanOrEqualTo(1);
        }

        @Test
        @DisplayName("Должен добавлять новое действие пользователя в таблицу audit")
        void shouldInsertUserAction() {
            int userId = 1;
            String action = "Пользователь сохранил новую тренировку.";
            Pageable pageable = new Pageable(0, Integer.MAX_VALUE);

            auditDao.insertUserAction(userId, action);

            assertThat(auditDao.getUserActions(userId, pageable))
                    .extracting(Audit::getAction)
                    .contains(action);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Должен возвращать пустой список, если пользователь не найден")
        void shouldReturnEmptyListIfUserNotFound() {
            int nonExistentUserId = -1;
            Pageable pageable = new Pageable(0, 10);

            assertThat(auditDao.getUserActions(nonExistentUserId, pageable))
                    .isEmpty();
        }
    }
}