package com.ylab.intensive.repository.extractor;

import com.ylab.intensive.model.entity.Workout;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

/**
 * The {@code WorkoutExtractor} class is an implementation of the
 * {@code ResultSetExtractor<Workout>} interface for extracting
 * a {@code Workout} object from a {@code ResultSet}.
 */
@Component
public class WorkoutExtractor implements ResultSetExtractor<Workout> {

    /**
     * Extracts a {@code Workout} object from the provided {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} containing the data.
     * @return A {@code Workout} object extracted from the {@code ResultSet},
     * or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public Workout extractData(ResultSet rs) throws SQLException {
        Workout workout = null;
        if (rs.next()) {
            workout = new Workout();
            workout.setId(rs.getInt("id"));
            workout.setUuid((UUID) rs.getObject("uuid"));
            workout.setUserId(rs.getInt("user_id"));
            workout.setType(rs.getString("workout_type"));
            workout.setDate(rs.getDate("date").toLocalDate());

            Duration durationFromDB = Duration.ofSeconds(rs.getInt("duration"));
            workout.setDuration(durationFromDB);

            workout.setCalorie(rs.getFloat("calorie"));

            workout.setWorkoutInfo(new HashMap<>());
        }
        return workout;
    }
}
