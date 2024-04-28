package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.model.entity.Audit;
import com.ylab.intensive.util.SQLExceptionUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation class for {@link AuditDao}.
 */
@Log4j2
@ApplicationScoped
@NoArgsConstructor
public class AuditDaoImpl implements AuditDao {

    @Override
    public List<Audit> getUserActions(int userId) {
        List<Audit> actions = new ArrayList<>();

        String FIND_ALL_AUDIT = """
                    SELECT a.id, a.user_id, a.date_of_action, a.action
                    FROM internal.audit a
                    WHERE a.user_id = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_AUDIT)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LocalDateTime dateOfSubmission = resultSet.getTimestamp("date_of_action").toLocalDateTime();
                Audit audit = new Audit(resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        dateOfSubmission,
                        resultSet.getString("action"));
                actions.add(audit);
            }
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return actions;
    }

    @Override
    public void insertUserAction(int userId, String action) {
        String SAVE = """
                insert into internal.audit(user_id, action, date_of_action) values (?,?,CURRENT_TIMESTAMP)
                """;
        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, action);

                preparedStatement.executeUpdate();
            } catch (SQLException exc) {
                connection.rollback();
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
    }
}
