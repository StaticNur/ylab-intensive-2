package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.service.ConnectionManager;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the WorkoutDao interface.
 * This class provides methods to interact with workout data in the database.
 */
public class WorkoutDaoImpl implements WorkoutDao {
    @Override
    public Optional<Workout> findByDate(LocalDate date) {
        String FIND_BY_EMAIL = """
                SELECT w.id, w.user_id, w.date, w.duration, w.calorie
                FROM internal.workout w
                WHERE w.date = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {
            statement.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {

                    Workout workout = new Workout();
                    workout.setId(rs.getInt("id"));
                    workout.setUserId(rs.getInt("user_id"));
                    workout.setDate(rs.getDate("date").toLocalDate());

                    Duration durationFromDB = Duration.ofSeconds(rs.getInt("duration"));
                    workout.setDuration(durationFromDB);

                    workout.setCalorie(rs.getFloat("calorie"));
                    return Optional.of(workout);
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

            try(PreparedStatement statement = connection.prepareStatement(INSERT_WORKOUT, Statement.RETURN_GENERATED_KEYS)){
                statement.setInt(1, workout.getUserId());
                statement.setDate(2, Date.valueOf(workout.getDate()));
                statement.setInt(3, (int) workout.getDuration().getSeconds());
                statement.setFloat(4, workout.getCalorie());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating workout failed, no rows affected.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        workout.setId(generatedKeys.getInt("id"));
                    } else {
                        throw new SQLException("Creating workout failed, no ID obtained.");
                    }
                }
            }catch (SQLException e) {
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
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_WORKOUT)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Workout workout = new Workout();
                    workout.setId(rs.getInt("id"));
                    workout.setUserId(rs.getInt("user_id"));
                    workout.setDate(rs.getDate("date").toLocalDate());

                    Duration durationFromDB = Duration.ofSeconds(rs.getInt("duration"));
                    workout.setDuration(durationFromDB);

                    workout.setCalorie(rs.getFloat("calorie"));

                    workoutList.add(workout);
                }
            }
        } catch (SQLException | DaoException e) {
            throw new DaoException("Error fetching workouts. " + e.getMessage());
        }
        return workoutList;
    }

    @Override
    public void deleteWorkout(LocalDate date) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout WHERE date = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(DELETE_WORKOUT)){
                statement.setDate(1, Date.valueOf(date));

                statement.executeUpdate();
            }catch (SQLException e) {
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

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_CALORIE)){
                statement.setFloat(1, calorie);
                statement.setInt(2, id);

                statement.executeUpdate();
            }catch (SQLException e) {
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

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_DURATION)){
                statement.setInt(1, (int) duration.getSeconds());
                statement.setInt(2, id);

                statement.executeUpdate();
            }catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Error updateDuration workout. " + e.getMessage());
        }
    }

    @Override
    public List<Workout> findByDuration(LocalDate begin, LocalDate end) {
        List<Workout> workouts = new ArrayList<>();
        String FIND_BY_DURATION = """
                                SELECT w.id, w.user_id, w.date, w.duration, w.calorie
                                FROM internal.workout w WHERE date BETWEEN ? AND ?
                                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_DURATION)) {
            statement.setDate(1, Date.valueOf(begin));
            statement.setDate(2, Date.valueOf(end));

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Workout workout = new Workout();
                    workout.setId(rs.getInt("id"));
                    workout.setUserId(rs.getInt("user_id"));
                    workout.setDate(rs.getDate("date").toLocalDate());

                    Duration durationFromDB = Duration.ofSeconds(rs.getInt("duration"));
                    workout.setDuration(durationFromDB);

                    workout.setCalorie(rs.getFloat("calorie"));
                    workouts.add(workout);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error findByDuration workout. " + e.getMessage());
        }

        return workouts;
    }
}
