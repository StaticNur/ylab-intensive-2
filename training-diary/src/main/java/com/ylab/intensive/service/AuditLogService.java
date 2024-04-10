package com.ylab.intensive.service;

public interface AuditLogService {
    void logAction(String username, String action);
}
