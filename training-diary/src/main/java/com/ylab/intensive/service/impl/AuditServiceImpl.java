package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation class for {@link AuditService}.
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    /**
     * This DAO is responsible for data access operations related to audit.
     */
    private final AuditDao auditDao;

    @Override
    @Transactional
    public void saveAction(int userId, String action) {
        auditDao.insertUserAction(userId, action);
    }

    @Override
    public List<Audit> getAudit(int userId, Pageable pageable) {
        return auditDao.getUserActions(userId, pageable);
    }
}
