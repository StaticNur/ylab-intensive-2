package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.container.PostgresTestContainer;
import com.ylab.intensive.dao.container.TestConfigurationEnvironment;
import com.ylab.intensive.model.enums.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Role Database Operations Testing")
class RoleDaoImplTest extends TestConfigurationEnvironment {

    private static RoleDaoImpl roleDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        roleDao = new RoleDaoImpl();
    }

    @Test
    @DisplayName("Find role by name - role exists")
    void testFindByName_RoleExists() {
        Role role = Role.ADMIN;

        int roleId = roleDao.findByName(role);

        assertThat(roleId).isPositive();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
