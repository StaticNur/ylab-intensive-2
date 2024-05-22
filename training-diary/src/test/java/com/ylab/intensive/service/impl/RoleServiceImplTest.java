package com.ylab.intensive.service.impl;

import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.repository.RoleDao;
import com.ylab.intensive.tag.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Role Service Tests")
public class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    @DisplayName("Get role ID by name - success")
    void testGetIdByName_Success() {
        Role role = Role.USER;
        int expectedId = 1;

        when(roleDao.findByName(role)).thenReturn(expectedId);

        assertThat(roleService.getIdByName(role))
                .isEqualTo(expectedId);
    }
}
