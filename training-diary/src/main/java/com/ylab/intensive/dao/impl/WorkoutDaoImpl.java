package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.config.ConnectionManager;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the WorkoutDao interface.
 * This class provides methods to interact with workout data in the database.
 */
public class WorkoutDaoImpl implements WorkoutDao {

    @Override
    public Optional<Workout> findByDate(LocalDate date, int userId) {
        String FIND_BY_DATE = """
                SELECT w.id, w.user_id, w.date, w.duration, w.calorie
                FROM internal.workout w
                WHERE w.date = ? and w.user_id = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DATE)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setInt(2, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(buildWorkout(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Workout saveWorkout(Workout workout) {
        String INSERT_WORKOUT = "INSERT INTO internal.workout (user_id, date, duration, calorie) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WORKOUT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, workout.getUserId());
                preparedStatement.setDate(2, Date.valueOf(workout.getDate()));
                preparedStatement.setInt(3, (int) workout.getDuration().getSeconds());
                preparedStatement.setFloat(4, workout.getCalorie());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating workout failed, no rows affected.");
                }

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        workout.setId(generatedKeys.getInt("id"));
                    } else {
                        throw new SQLException("Creating workout failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
            return workout;
        } catch (SQLException e) {
            throw new DaoException("Error saving workout. " + e.getMessage());
        }
    }

    @Override
    public List<Workout> findByUserId(int userId) {
        List<Workout> workoutList = new ArrayList<>();
        String FIND_ALL_WORKOUT = """
                SELECT w.id, w.user_id, w.date, w.duration, w.calorie
                FROM internal.workout w where w.user_id = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WORKOUT)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    workoutList.add(buildWorkout(rs));
                }
            }
        } catch (SQLException | DaoException e) {
            throw new DaoException("Error fetching workouts. " + e.getMessage());
        }
        return workoutList;
    }

    @Override
    public void deleteWorkout(LocalDate date, int userId) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout WHERE date = ? and user_id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKOUT)) {
                preparedStatement.setDate(1, Date.valueOf(date));
                preparedStatement.setInt(2, userId);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error delete workout. " + e.getMessage());
        }
    }

    @Override
    public void updateCalorie(int id, Float calorie) {
        String UPDATE_CALORIE = "UPDATE internal.workout SET calorie = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CALORIE)) {
                preparedStatement.setFloat(1, calorie);
                preparedStatement.setInt(2, id);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error updating calorie for workout. " + e.getMessage());
        }
    }


    @Override
    public void updateDuration(int id, Duration duration) {
        String UPDATE_DURATION = "UPDATE internal.workout SET duration = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DURATION)) {
                preparedStatement.setInt(1, (int) duration.getSeconds());
                preparedStatement.setInt(2, id);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error updateDuration workout. " + e.getMessage());
        }
    }

    @Override
    public List<Workout> findByDuration(int userId, LocalDate begin, LocalDate end) {
        List<Workout> workouts = new ArrayList<>();
        String FIND_BY_DURATION = """
                SELECT w.id, w.user_id, w.date, w.duration, w.calorie
                FROM internal.workout w WHERE w.user_id = ? and w.date BETWEEN ? AND ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DURATION)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setDate(2, Date.valueOf(begin));
            preparedStatement.setDate(3, Date.valueOf(end));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    workouts.add(buildWorkout(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error findByDuration workout. " + e.getMessage());
        }

        return workouts;
    }

    private Workout buildWorkout(ResultSet rs) throws SQLException {
        Workout workout = new Workout();
        workout.setId(rs.getInt("id"));
        workout.setUserId(rs.getInt("user_id"));
        workout.setDate(rs.getDate("date").toLocalDate());

        Duration durationFromDB = Duration.ofSeconds(rs.getInt("duration"));
        workout.setDuration(durationFromDB);

        workout.setCalorie(rs.getFloat("calorie"));
        return workout;
    }
}
