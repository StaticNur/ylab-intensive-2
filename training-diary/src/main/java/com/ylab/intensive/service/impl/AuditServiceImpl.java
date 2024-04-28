package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.service.AuditService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Implementation class for {@link AuditService}.
 */
@ApplicationScoped
@NoArgsConstructor
public class AuditServiceImpl implements AuditService {

    /**
     * This DAO is responsible for data access operations related to audit.
     */
    private AuditDao auditDao;

    @Inject
    public AuditServiceImpl(AuditDao auditDao) {
        this.auditDao = auditDao;
    }

    @Override
    public void saveAction(int userId, String action) {
        auditDao.insertUserAction(userId, action);
    }

    @Override
    @Timed
    @Loggable
    public List<Audit> getAudit(int userId) {
        return auditDao.getUserActions(userId);
    }
}
