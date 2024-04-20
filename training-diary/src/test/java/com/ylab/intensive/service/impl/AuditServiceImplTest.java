package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.AuditDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Audit Service Tests")
public class AuditServiceImplTest {

    @Mock
    private AuditDao auditDao;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    @DisplayName("Save user action - success")
    void testSaveAction_Success() {
        int userId = 1;
        String action = "User login";

        doNothing().when(auditDao).insertUserAction(userId, action);

        auditService.saveAction(userId, action);

        verify(auditDao).insertUserAction(userId, action);
    }

    @Test
    @DisplayName("Get user audit - success")
    void testGetAudit_Success() {
        int userId = 1;
        List<String> mockActions = Arrays.asList("User login", "User logout");

        when(auditDao.getUserActions(userId)).thenReturn(mockActions);

        List<String> result = auditService.getAudit(userId);

        assertThat(result).isEqualTo(mockActions);
    }
}