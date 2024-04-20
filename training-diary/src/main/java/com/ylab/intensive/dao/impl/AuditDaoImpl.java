package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.service.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class for {@link AuditDao}.
 */
public class AuditDaoImpl implements AuditDao {

    @Override
    public List<String> getUserActions(int userId) {
        List<String> actions = new ArrayList<>();

        String FIND_ALL_AUDIT = """
                    SELECT action
                    FROM internal.audit a
                    WHERE a.user_id = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_AUDIT)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    actions.add(resultSet.getString("action"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return actions;
    }

    @Override
    public void insertUserAction(int userId, String action) {
        String SAVE = """
                insert into internal.audit(user_id, action) values (?,?)
                """;
        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, action);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
