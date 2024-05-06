package com.ylab.intensive.repository.impl;

import com.ylab.intensive.repository.WorkoutInfoDao;
import com.ylab.intensive.repository.extractor.WorkoutInfoExtractor;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.WorkoutInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation class for {@link WorkoutInfoDao}.
 */
@Log4j2
@Repository
@RequiredArgsConstructor
public class WorkoutInfoDaoImpl implements WorkoutInfoDao {
    /**
     * The JDBC template used for executing SQL queries against the database.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * The WorkoutInfo extractor responsible for extracting WorkoutInfo information
     * from the provided {@code ResultSet}.
     */
    private final WorkoutInfoExtractor extractor;

    public void saveWorkoutInfo(int workoutId, String title, String info) {
        String INSERT_INFO = """
                INSERT INTO internal.workout_info (workout_id, title, info) VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(INSERT_INFO, workoutId, title, info);
    }

    @Override
    public void updateWorkoutInfo(int workoutId, String title, String info) {
        String UPDATE_INFO = """
                UPDATE internal.workout_info SET info = ? WHERE workout_id = ? AND title = ?
                """;
        int affectedRows = jdbcTemplate.update(UPDATE_INFO, info, workoutId, title);
        if (affectedRows == 0) {
            throw new DaoException("Такого заголовка в доп. инфо. тренировки нет! Сначала добавьте ее.");
        }
    }

    @Override
    public Optional<WorkoutInfo> findByWorkoutId(int workoutId) {
        String FIND_BY_WORKOUT_ID = """
                SELECT id, title, info FROM internal.workout_info WHERE workout_id = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_WORKOUT_ID, extractor, workoutId));
    }

    @Override
    public void delete(int workoutId) {
        String DELETE_WORKOUT = """
                DELETE FROM internal.workout_info WHERE workout_id = ?
                """;
        int rowsAffected = jdbcTemplate.update(DELETE_WORKOUT, workoutId);
        if (rowsAffected == 0) {
            throw new DaoException("Deleting workout_info failed, no rows affected.");
        }
    }
}
