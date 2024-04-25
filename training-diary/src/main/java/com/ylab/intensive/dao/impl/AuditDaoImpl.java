package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.util.DaoUtil;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class for {@link AuditDao}.
 */
@Log4j2
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

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                actions.add(resultSet.getString("action"));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
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
            } catch (SQLException exc) {
                connection.rollback();
                DaoUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
    }
}
