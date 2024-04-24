package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.service.AuditService;

import java.util.List;

/**
 * Implementation class for {@link AuditService}.
 */
public class AuditServiceImpl implements AuditService {

    /**
     * This DAO is responsible for data access operations related to audit.
     */
    @Inject
    private AuditDao auditDao;

    @Override
    public void saveAction(int userId, String action) {
        auditDao.insertUserAction(userId, action);
    }

    @Override
    public List<String> getAudit(int userId) {
        return auditDao.getUserActions(userId);
    }
}
