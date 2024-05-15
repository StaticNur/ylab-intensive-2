package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.repository.RoleDao;
import com.ylab.intensive.repository.config.TestRepositories;
import com.ylab.intensive.tag.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тесты для реализации RoleDao")
class RoleDaoImplTest {

    @Autowired
    private RoleDao roleDao;

    @Test
    @DisplayName("Должен находить идентификатор роли по имени")
    void shouldFindRoleIdByName() {
        Role role = Role.USER;
        Integer expectedRoleId = 2;

        Integer actualRoleId = roleDao.findByName(role);

        Assertions.assertThat(actualRoleId).isEqualTo(expectedRoleId);
    }
}