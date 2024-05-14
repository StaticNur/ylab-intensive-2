package com.ylab.intensive.repository.extractor;

import com.ylab.intensive.model.entity.WorkoutType;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code WorkoutTypeExtractor} class is an implementation of the
 * {@code ResultSetExtractor<WorkoutType>} interface for extracting
 * a {@code WorkoutType} object from a {@code ResultSet}.
 */
@Component
public class WorkoutTypeExtractor implements ResultSetExtractor<WorkoutType> {

    /**
     * Extracts a {@code WorkoutType} object from the provided {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} containing the data.
     * @return A {@code WorkoutType} object extracted from the {@code ResultSet},
     * or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public WorkoutType extractData(ResultSet rs) throws SQLException {
        WorkoutType workoutType = null;
        if (rs.next()) {
            workoutType = new WorkoutType();
            workoutType.setId(rs.getInt("id"));
            workoutType.setUserId(rs.getInt("user_id"));
            workoutType.setType(rs.getString("type"));
        }
        return workoutType;
    }
}
