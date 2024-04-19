package com.ylab.intensive.dao;

import java.util.List;

public interface AuditDao {
    List<String> getUserActions(int userId);

    void insertUserAction(int userId, String action);
}
