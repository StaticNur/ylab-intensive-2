package com.ylab.intensive.repository.impl;

import com.ylab.intensive.repository.WorkoutDao;
import com.ylab.intensive.repository.extractor.ListExtractor;
import com.ylab.intensive.repository.extractor.WorkoutExtractor;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.Workout;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final WorkoutExtractor extractor;

    @Override
    public Optional<Workout> findByDate(LocalDate date, int userId) {
        String FIND_BY_DATE = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
                FROM internal.workout w
                WHERE w.date = ? and w.user_id = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_DATE, extractor, date, userId));
    }

    @Override
    public Workout saveWorkout(Workout workout) {
        String INSERT_WORKOUT = """
                INSERT INTO internal.workout (uuid, user_id, workout_type, date, duration, calorie) VALUES (?, ?, ?, ?, ?, ?)
                """;
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

        if (keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty()) {
            Map<String, Object> keys = keyHolder.getKeys();
            Number id = (Number) keys.get("id");
            workout.setId(id.intValue());
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
        return jdbcTemplate.query(FIND_ALL_WORKOUT, new ListExtractor<>(extractor), userId);
    }

    @Override
    public void deleteWorkout(int userId, int id) {
        String DELETE_WORKOUT = """
                DELETE FROM internal.workout WHERE id = ? and user_id = ?
                """;
        int rowsAffected = jdbcTemplate.update(DELETE_WORKOUT, id, userId);
        if (rowsAffected == 0) {
            throw new DaoException("Deleting workout failed, no rows affected.");
        }
    }

    @Override
    public void updateCalorie(int id, Float calorie) {
        String UPDATE_CALORIE = """
                UPDATE internal.workout SET calorie = ? WHERE id = ?
                """;
        int rowsAffected = jdbcTemplate.update(UPDATE_CALORIE, calorie, id);
        if (rowsAffected == 0) {
            throw new DaoException("Updating calorie failed, no rows affected.");
        }
    }

    @Override
    public void updateDuration(int id, Duration duration) {
        String UPDATE_DURATION = """
                UPDATE internal.workout SET duration = ? WHERE id = ?
                """;
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
        return jdbcTemplate.query(FIND_BY_DURATION, new ListExtractor<>(extractor), userId, begin, end);
    }

    @Override
    public Optional<Workout> findByUUID(UUID uuid) {
        String FIND_ALL_WORKOUT = """
                SELECT w.id, w.uuid, w.user_id, w.workout_type, w.date, w.duration, w.calorie
                FROM internal.workout w
                WHERE w.uuid = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(FIND_ALL_WORKOUT, extractor, uuid));
    }

    @Override
    public void updateType(int workoutId, String newType) {
        String UPDATE_TYPE = """
                UPDATE internal.workout SET workout_type = ? WHERE id = ?
                """;
        int rowsAffected = jdbcTemplate.update(UPDATE_TYPE, newType, workoutId);
        if (rowsAffected == 0) {
            throw new DaoException("Updating workout type failed, no rows affected.");
        }
    }
}
