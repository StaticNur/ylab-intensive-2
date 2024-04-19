package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.service.AuditService;

import java.util.List;

public class AuditServiceImpl implements AuditService {

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
