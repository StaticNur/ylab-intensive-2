package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutInfoDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.util.SQLExceptionUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation class for {@link WorkoutInfoDao}.
 */
@Log4j2
@ApplicationScoped
@NoArgsConstructor
public class WorkoutInfoDaoImpl implements WorkoutInfoDao {

    @Override
    public void saveWorkoutInfo(int workoutId, String title, String info) {
        String INSERT_INFO = "INSERT INTO internal.workout_info (workout_id, title, info) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INFO)) {
                preparedStatement.setInt(1, workoutId);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, info);

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

    @Override
    public void updateWorkoutInfo(int workoutId, String title, String info) {
        String UPDATE_INFO = "UPDATE internal.workout_info SET info = ? WHERE workout_id = ? AND title = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INFO)) {
                preparedStatement.setString(1, info);
                preparedStatement.setInt(2, workoutId);
                preparedStatement.setString(3, title);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Такого заголовка в доп. инфо. тренировки нет! Сначала добавьте ее.");
                }
            } catch (SQLException exc) {
                connection.rollback();
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
    }

    @Override
    public WorkoutInfo findByWorkoutId(int workoutId) {
        WorkoutInfo workoutInfo = new WorkoutInfo();
        Map<String, String> workoutInfoMap = new HashMap<>();
        String FIND_BY_WORKOUT_ID = "SELECT id, title, info FROM internal.workout_info WHERE workout_id = ?";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_WORKOUT_ID)) {

            preparedStatement.setInt(1, workoutId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                workoutInfo.setId(resultSet.getInt("id"));
                String key = resultSet.getString("title");
                String value = resultSet.getString("info");
                workoutInfoMap.put(key, value);
            }

        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        workoutInfo.setWorkoutInfo(workoutInfoMap);
        workoutInfo.setWorkoutId(workoutId);
        return workoutInfo;
    }

    @Override
    public void delete(int workoutId) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout_info WHERE workout_id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKOUT)) {
                preparedStatement.setInt(1, workoutId);

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
