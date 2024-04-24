package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.RoleDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.config.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation class for {@link RoleDao}.
 */
public class RoleDaoImpl implements RoleDao {
    @Override
    public int findByName(Role role) {
        String FIND_BY_NAME = """
                SELECT r.id
                FROM internal.role r
                WHERE r.name = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, role.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new DaoException("Role not found.");
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
