package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.repository.RoleDao;
import com.ylab.intensive.repository.container.PostgresTestContainer;
import com.ylab.intensive.repository.container.TestConfigurationEnvironment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты для реализации RoleDaoImpl")
public class RoleDaoImplTest extends TestConfigurationEnvironment {

    private static RoleDao roleDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        JdbcTemplate jdbcTemplate = PostgresTestContainer.getJdbcTemplate();
        roleDao = new RoleDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("Должен находить идентификатор роли по имени")
    void shouldFindRoleIdByName() {
        Role role = Role.USER;
        Integer expectedRoleId = 2;

        Integer actualRoleId = roleDao.findByName(role);

        assertThat(actualRoleId).isEqualTo(expectedRoleId);
    }
    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
