package com.ylab.intensive.repository.impl;

import com.ylab.intensive.repository.WorkoutTypeDao;
import com.ylab.intensive.repository.extractor.ListExtractor;
import com.ylab.intensive.repository.extractor.WorkoutTypeExtractor;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.WorkoutType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation class for {@link WorkoutTypeDao}.
 */
@Log4j2
@Repository
@RequiredArgsConstructor
public class WorkoutTypeDaoImpl implements WorkoutTypeDao {

    private final JdbcTemplate jdbcTemplate;
    private final WorkoutTypeExtractor extractor;

    public WorkoutType saveType(int userId, String type) {
        String INSERT_TYPE = """
                INSERT INTO internal.workout_type (user_id, type) VALUES (?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_TYPE, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setString(2, type);
            return ps;
        }, keyHolder);

        WorkoutType workoutType = new WorkoutType();
        if (keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty()) {
            Map<String, Object> keys = keyHolder.getKeys();
            Number id = (Number) keys.get("id");
            workoutType.setId(id.intValue());
        } else {
            throw new DaoException("Creating workout type failed, no ID obtained.");
        }

        workoutType.setUserId(userId);
        workoutType.setType(type);
        return workoutType;
    }

    @Override
    public void updateType(int userId, String oldType, String newType) {
        String UPDATE_TYPE = """
                UPDATE internal.workout_type SET type = ? WHERE user_id = ? AND type = ?
                """;
        int affectedRows = jdbcTemplate.update(UPDATE_TYPE, newType, userId, oldType);

        if (affectedRows == 0) {
            throw new DaoException("Updating workout type failed, no rows affected.");
        }
    }

    @Override
    public List<WorkoutType> findByUserId(int userId) {
        String FIND_BY_USER_ID = """
                SELECT id, user_id, type FROM internal.workout_type WHERE user_id = ?
                """;
        return jdbcTemplate.query(FIND_BY_USER_ID, new ListExtractor<>(extractor), userId);
    }

    @Override
    public void delete(int userId) {
        String DELETE_WORKOUT = """
                DELETE FROM internal.workout_type WHERE user_id = ?
                """;
        jdbcTemplate.update(DELETE_WORKOUT, userId);
    }

    public Optional<WorkoutType> findByName(String name) {
        String FIND_BY_ID = """
                SELECT id, user_id, type FROM internal.workout_type WHERE type = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_ID, extractor, name));
    }

    @Override
    public Optional<WorkoutType> findTypeByUserId(int userId, String typeName) {
        String FIND_BY_TYPE = """
                SELECT id, user_id, type FROM internal.workout_type WHERE user_id = ? AND type = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_TYPE, extractor, userId, typeName));
    }
}
