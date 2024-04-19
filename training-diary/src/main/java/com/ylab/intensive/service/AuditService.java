package com.ylab.intensive.service;

import java.util.List;

public interface AuditService {
    void saveAction(int userId, String action);

    List<String> getAudit(int userId);
}
