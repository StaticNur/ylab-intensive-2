package com.ylab.intensive.dao;

import com.ylab.intensive.model.entity.User;

import java.util.List;

public interface AuditDao {
    List<String> getUserActions(int userId);

    void insertUserAction(int userId, String action);
}
