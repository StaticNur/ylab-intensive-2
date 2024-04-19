package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.service.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditDaoImpl implements AuditDao {
    @Override
    public List<String> getUserActions(int userId) {
        List<String> actions = new ArrayList<>();

        String SELECT_ACTIONS_FOR_USER = """
                    SELECT *
                    FROM internal.audit a
                    WHERE a.user_id = ?
                """;

        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SELECT_ACTIONS_FOR_USER)) {
            statement.setInt(1, userId);
            try (var resultSet = statement.executeQuery();){
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
        try (var connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);
            try (var statement = connection.prepareStatement(SAVE)){
                statement.setInt(1, userId);
                statement.setString(2, action);

                statement.executeUpdate();
            }catch (SQLException e){
                connection.rollback();
                throw new DaoException(e.getMessage());
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
