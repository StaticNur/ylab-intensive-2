package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutTypeDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.service.ConnectionManager;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class WorkoutTypeDaoImpl implements WorkoutTypeDao {
    @Override
    public void saveType(int workoutId, String type) {
        String INSERT_TYPE = "INSERT INTO internal.workout_type (workout_id, type) VALUES (?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try ( PreparedStatement statement = connection.prepareStatement(INSERT_TYPE)){
                statement.setInt(1, workoutId);
                statement.setString(2, type);

                statement.executeUpdate();
            }catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error saving workout type. " + e.getMessage());
        }
    }

    @Override
    public void updateType(int workoutId, String oldType, String newType) {
        String UPDATE_TYPE = "UPDATE internal.workout_type SET type = ? WHERE workout_id = ? AND type = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try ( PreparedStatement statement = connection.prepareStatement(UPDATE_TYPE)){
                statement.setString(1, newType);
                statement.setInt(2, workoutId);
                statement.setString(3, oldType);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Updating workout type failed, no rows affected.");
                }
            }catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error updating workout type. " + e.getMessage());
        }
    }

    @Override
    public Set<String> findByWorkoutId(int workoutId) {
        Set<String> types = new HashSet<>();
        String SELECT_TYPES = "SELECT type FROM internal.workout_type WHERE workout_id = ?";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_TYPES)) {

            statement.setInt(1, workoutId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    types.add(resultSet.getString("type"));
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Error finding types by workoutId. " + e.getMessage());
        }

        return types;
    }

    @Override
    public void delete(int workoutId) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout_type WHERE workout_id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(DELETE_WORKOUT)){
                statement.setInt(1, workoutId);

                statement.executeUpdate();
            }catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error delete workout_type. " + e.getMessage());
        }
    }
}
