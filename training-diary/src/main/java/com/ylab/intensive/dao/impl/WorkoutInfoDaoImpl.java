package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutInfoDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.service.ConnectionManager;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class WorkoutInfoDaoImpl implements WorkoutInfoDao {
    @Override
    public void saveWorkoutInfo(int workoutId, String title, String info) {
        String INSERT_INFO = "INSERT INTO internal.workout_info (workout_id, title, info) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(INSERT_INFO)){
                statement.setInt(1, workoutId);
                statement.setString(2, title);
                statement.setString(3, info);

                statement.executeUpdate();
            }catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error saving workout info. " + e.getMessage());
        }
    }


    @Override
    public void updateWorkoutInfo(int workoutId, String title, String info) {
        String UPDATE_INFO = "UPDATE internal.workout_info SET info = ? WHERE workout_id = ? AND title = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_INFO)){
                statement.setString(1, info);
                statement.setInt(2, workoutId);
                statement.setString(3, title);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Updating workout info failed, no rows affected.");
                }
            }catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error updating workout info. " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> findByWorkoutId(int workoutId) {
        Map<String, String> workoutInfoMap = new HashMap<>();
        String SELECT_INFO = "SELECT title, info FROM internal.workout_info WHERE workout_id = ?";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_INFO)) {

            statement.setInt(1, workoutId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String key = resultSet.getString("title");
                    String value = resultSet.getString("info");
                    workoutInfoMap.put(key, value);
                }
            }

        } catch (SQLException e) {
            throw new DaoException("Error finding workout info by workoutId. " + e.getMessage());
        }

        return workoutInfoMap;
    }

    @Override
    public void delete(int workoutId) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout_info WHERE workout_id = ?";

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
            throw new DaoException("Error delete workout_info. " + e.getMessage());
        }
    }

}
