package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.util.DaoUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

/**
 * Implementation of the WorkoutDao interface.
 * This class provides methods to interact with workout data in the database.
 */
@Log4j2
@ApplicationScoped
@NoArgsConstructor
public class WorkoutDaoImpl implements WorkoutDao {

    @Override
    public Optional<Workout> findByDate(LocalDate date, int userId) {
        String FIND_BY_DATE = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type_id, w.date, w.duration, w.calorie
                FROM internal.workout w
                WHERE w.date = ? and w.user_id = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DATE)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setInt(2, userId);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return Optional.of(buildWorkout(rs));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    @Override
    public Workout saveWorkout(Workout workout) {
        String INSERT_WORKOUT = "INSERT INTO internal.workout (uuid, user_id, workout_type_id, date, duration, calorie) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WORKOUT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setObject(1, workout.getUuid());
                preparedStatement.setInt(2, workout.getUserId());
                preparedStatement.setInt(3, Integer.parseInt(workout.getType()));
                preparedStatement.setDate(4, Date.valueOf(workout.getDate()));
                preparedStatement.setInt(5, (int) workout.getDuration().getSeconds());
                preparedStatement.setFloat(6, workout.getCalorie());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Creating workout failed, no rows affected.");
                }

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    workout.setId(generatedKeys.getInt("id"));
                } else {
                    throw new DaoException("Creating workout failed, no ID obtained.");
                }
            } catch (SQLException exc) {
                connection.rollback();
                DaoUtil.handleSQLException(exc, log);
            }

            connection.commit();
            return workout;
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
            return new Workout();
        }
    }

    @Override
    public List<Workout> findByUserId(int userId) {
        List<Workout> workoutList = new ArrayList<>();
        String FIND_ALL_WORKOUT = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type_id, w.date, w.duration, w.calorie
                FROM internal.workout w where w.user_id = ? ORDER BY w.date
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WORKOUT)) {

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                workoutList.add(buildWorkout(rs));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return workoutList;
    }

    @Override
    public void deleteWorkout(int userId, int id) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout WHERE id = ? and user_id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKOUT)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, userId);

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
    public void updateCalorie(int id, Float calorie) {
        String UPDATE_CALORIE = "UPDATE internal.workout SET calorie = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CALORIE)) {
                preparedStatement.setFloat(1, calorie);
                preparedStatement.setInt(2, id);

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
    public void updateDuration(int id, Duration duration) {
        String UPDATE_DURATION = "UPDATE internal.workout SET duration = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DURATION)) {
                preparedStatement.setInt(1, (int) duration.getSeconds());
                preparedStatement.setInt(2, id);

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
    public List<Workout> findByDuration(int userId, LocalDate begin, LocalDate end) {
        List<Workout> workouts = new ArrayList<>();
        String FIND_BY_DURATION = """
                SELECT w.id, w.uuid, w.user_id, workout_type_id, w.date, w.duration, w.calorie
                FROM internal.workout w WHERE w.user_id = ? and w.date BETWEEN ? AND ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DURATION)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setDate(2, Date.valueOf(begin));
            preparedStatement.setDate(3, Date.valueOf(end));

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                workouts.add(buildWorkout(rs));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }

        return workouts;
    }

    @Override
    public Optional<Workout> findByUUID(UUID uuid) {
        String FIND_ALL_WORKOUT = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type_id, w.date, w.duration, w.calorie
                FROM internal.workout w where w.uuid = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WORKOUT)) {

            preparedStatement.setObject(1, uuid);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return Optional.of(buildWorkout(rs));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    @Override
    public void updateType(int workoutId, int typeId) {
        String UPDATE_TYPE = "UPDATE internal.workout SET workout_type_id = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TYPE)) {
                preparedStatement.setInt(1, typeId);
                preparedStatement.setInt(2, workoutId);

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

    private Workout buildWorkout(ResultSet rs) throws SQLException {
        Workout workout = new Workout();
        workout.setId(rs.getInt("id"));
        workout.setUuid((UUID) rs.getObject("uuid"));
        workout.setUserId(rs.getInt("user_id"));
        workout.setType(String.valueOf(rs.getInt("workout_type_id")));
        workout.setDate(rs.getDate("date").toLocalDate());

        Duration durationFromDB = Duration.ofSeconds(rs.getInt("duration"));
        workout.setDuration(durationFromDB);

        workout.setCalorie(rs.getFloat("calorie"));

        workout.setWorkoutInfo(new HashMap<>());
        return workout;
    }
}
