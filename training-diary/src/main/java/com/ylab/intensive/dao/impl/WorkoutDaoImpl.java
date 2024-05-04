package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.Workout;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
@Repository
@RequiredArgsConstructor
public class WorkoutDaoImpl implements WorkoutDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Workout> findByDate(LocalDate date, int userId) {
        String FIND_BY_DATE = """
            SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
            FROM internal.workout w
            WHERE w.date = ? and w.user_id = ?
            """;

        List<Workout> workouts = jdbcTemplate.query(FIND_BY_DATE,
                new Object[]{date, userId},
                (rs, rowNum) -> buildWorkout(rs));

        return workouts.stream().findFirst();
    }

    @Override
    public Workout saveWorkout(Workout workout) {
        String INSERT_WORKOUT = "INSERT INTO internal.workout (uuid, user_id, workout_type, date, duration, calorie) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int affectedRows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_WORKOUT, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, workout.getUuid());
            ps.setInt(2, workout.getUserId());
            ps.setString(3, workout.getType());
            ps.setDate(4, Date.valueOf(workout.getDate()));
            ps.setInt(5, (int) workout.getDuration().getSeconds());
            ps.setFloat(6, workout.getCalorie());
            return ps;
        }, keyHolder);

        if (affectedRows == 0) {
            throw new DaoException("Creating workout failed, no rows affected.");
        }

        if (keyHolder.getKey() != null) {
            workout.setId(keyHolder.getKey().intValue());
        } else {
            throw new DaoException("Creating workout failed, no ID obtained.");
        }

        return workout;
    }

    @Override
    public List<Workout> findByUserId(int userId) {
        String FIND_ALL_WORKOUT = """
            SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
            FROM internal.workout w
            WHERE w.user_id = ?
            ORDER BY w.date
            """;

        return jdbcTemplate.query(FIND_ALL_WORKOUT, new Object[]{userId}, (rs, rowNum) -> buildWorkout(rs));
    }

    @Override
    public void deleteWorkout(int userId, int id) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout WHERE id = ? and user_id = ?";
        int rowsAffected = jdbcTemplate.update(DELETE_WORKOUT, id, userId);
        if (rowsAffected == 0) {
            throw new DaoException("Deleting workout failed, no rows affected.");
        }
    }

    @Override
    public void updateCalorie(int id, Float calorie) {
        String UPDATE_CALORIE = "UPDATE internal.workout SET calorie = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(UPDATE_CALORIE, calorie, id);
        if (rowsAffected == 0) {
            throw new DaoException("Updating calorie failed, no rows affected.");
        }
    }

    @Override
    public void updateDuration(int id, Duration duration) {
        String UPDATE_DURATION = "UPDATE internal.workout SET duration = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(UPDATE_DURATION, (int) duration.getSeconds(), id);
        if (rowsAffected == 0) {
            throw new DaoException("Updating duration failed, no rows affected.");
        }
    }

    @Override
    public List<Workout> findByDuration(int userId, LocalDate begin, LocalDate end) {
        String FIND_BY_DURATION = """
            SELECT w.id, w.uuid, w.user_id, workout_type, w.date, w.duration, w.calorie
            FROM internal.workout w
            WHERE w.user_id = ? and w.date BETWEEN ? AND ?
            """;

        return jdbcTemplate.query(FIND_BY_DURATION, new Object[]{userId, begin, end}, (rs, rowNum) -> buildWorkout(rs));
    }

    @Override
    public Optional<Workout> findByUUID(UUID uuid) {
        String FIND_ALL_WORKOUT = """
            SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
            FROM internal.workout w
            WHERE w.uuid = ?
            """;

        List<Workout> workouts = jdbcTemplate.query(FIND_ALL_WORKOUT, new Object[]{uuid}, (rs, rowNum) -> buildWorkout(rs));
        return workouts.stream().findFirst();
    }

    @Override
    public void updateType(int workoutId, String newType) {
        String UPDATE_TYPE = "UPDATE internal.workout SET workout_type = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(UPDATE_TYPE, newType, workoutId);
        if (rowsAffected == 0) {
            throw new DaoException("Updating workout type failed, no rows affected.");
        }
    }
    /*@Override
    public Optional<Workout> findByDate(LocalDate date, int userId) {
        String FIND_BY_DATE = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
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
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    @Override
    public Workout saveWorkout(Workout workout) {
        String INSERT_WORKOUT = "INSERT INTO internal.workout (uuid, user_id, workout_type, date, duration, calorie) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WORKOUT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setObject(1, workout.getUuid());
                preparedStatement.setInt(2, workout.getUserId());
                preparedStatement.setString(3, workout.getType());
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
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
            return workout;
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
            return new Workout();
        }
    }

    @Override
    public List<Workout> findByUserId(int userId) {
        List<Workout> workoutList = new ArrayList<>();
        String FIND_ALL_WORKOUT = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
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
            SQLExceptionUtil.handleSQLException(exc, log);
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
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
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
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
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
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
    }

    @Override
    public List<Workout> findByDuration(int userId, LocalDate begin, LocalDate end) {
        List<Workout> workouts = new ArrayList<>();
        String FIND_BY_DURATION = """
                SELECT w.id, w.uuid, w.user_id, workout_type, w.date, w.duration, w.calorie
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
            SQLExceptionUtil.handleSQLException(exc, log);
        }

        return workouts;
    }

    @Override
    public Optional<Workout> findByUUID(UUID uuid) {
        String FIND_ALL_WORKOUT = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
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
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    @Override
    public void updateType(int workoutId, String newType) {
        String UPDATE_TYPE = "UPDATE internal.workout SET workout_type = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TYPE)) {
                preparedStatement.setString(1, newType);
                preparedStatement.setInt(2, workoutId);

                preparedStatement.executeUpdate();
            } catch (SQLException exc) {
                connection.rollback();
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
    }*/

    private Workout buildWorkout(ResultSet rs) throws SQLException {
        Workout workout = new Workout();
        workout.setId(rs.getInt("id"));
        workout.setUuid((UUID) rs.getObject("uuid"));
        workout.setUserId(rs.getInt("user_id"));
        workout.setType(rs.getString("workout_type"));
        workout.setDate(rs.getDate("date").toLocalDate());

        Duration durationFromDB = Duration.ofSeconds(rs.getInt("duration"));
        workout.setDuration(durationFromDB);

        workout.setCalorie(rs.getFloat("calorie"));

        workout.setWorkoutInfo(new HashMap<>());
        return workout;
    }
}
