package com.ylab.intensive.service.impl;

import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.repository.AuditDao;
import com.ylab.intensive.tag.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Audit Service Tests")
public class AuditServiceImplTest {

    @Mock
    private AuditDao auditDao;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Save user action - success")
        void testSaveAction_Success() {
            int userId = 1;
            String action = "User email";

            doNothing().when(auditDao).insertUserAction(userId, action);

            auditService.saveAction(userId, action);

            verify(auditDao).insertUserAction(userId, action);
        }

        @Test
        @DisplayName("Get user audit - success")
        void testGetAudit_Success() {
            int userId = 1;
            Pageable pageable = new Pageable(0, 10);
            List<Audit> mockActions = Arrays.asList(new Audit(1, 2, LocalDateTime.now(), "User email"),
                    new Audit(1, 2, LocalDateTime.now(), "User email"));

            when(auditDao.getUserActions(userId, pageable)).thenReturn(mockActions);

            assertThat(auditService.getAudit(userId, pageable))
                    .isEqualTo(mockActions);
        }
    }

}