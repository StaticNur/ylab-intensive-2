package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutTypeDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.util.DaoUtil;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation class for {@link WorkoutTypeDao}.
 */
@Log4j2
public class WorkoutTypeDaoImpl implements WorkoutTypeDao {

    @Override
    public void saveType(int workoutId, String type) {
        String INSERT_TYPE = "INSERT INTO internal.workout_type (workout_id, type) VALUES (?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TYPE)) {
                preparedStatement.setInt(1, workoutId);
                preparedStatement.setString(2, type);

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

    @Override
    public void updateType(int workoutId, String oldType, String newType) {
        String UPDATE_TYPE = "UPDATE internal.workout_type SET type = ? WHERE workout_id = ? AND type = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TYPE)) {
                preparedStatement.setString(1, newType);
                preparedStatement.setInt(2, workoutId);
                preparedStatement.setString(3, oldType);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Updating workout type failed, no rows affected.");
                }
            } catch (SQLException exc) {
                connection.rollback();
                DaoUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
    }

    @Override
    public Set<String> findByWorkoutId(int workoutId) {
        Set<String> types = new HashSet<>();
        String FIND_BY_WORKOUT_ID = "SELECT type FROM internal.workout_type WHERE workout_id = ?";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_WORKOUT_ID)) {

            preparedStatement.setInt(1, workoutId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                types.add(resultSet.getString("type"));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return types;
    }

    @Override
    public void delete(int workoutId) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout_type WHERE workout_id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKOUT)) {
                preparedStatement.setInt(1, workoutId);

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
