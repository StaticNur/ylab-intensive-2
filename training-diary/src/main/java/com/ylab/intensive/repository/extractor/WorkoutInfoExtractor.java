package com.ylab.intensive.repository.extractor;

import com.ylab.intensive.model.entity.WorkoutInfo;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code WorkoutInfoExtractor} class is an implementation of the
 * {@code ResultSetExtractor<WorkoutInfo>} interface for extracting
 * a {@code WorkoutInfo} object from a {@code ResultSet}.
 */
@Component
public class WorkoutInfoExtractor implements ResultSetExtractor<WorkoutInfo> {

    /**
     * Extracts a {@code WorkoutInfo} object from the provided {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} containing the data.
     * @return A {@code WorkoutInfo} object extracted from the {@code ResultSet},
     * or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public WorkoutInfo extractData(ResultSet rs) throws SQLException {
        WorkoutInfo workoutInfo = null;
        if (rs.next()) {
            workoutInfo = new WorkoutInfo();
            workoutInfo.setWorkoutId(rs.getInt("id"));
            Map<String, String> workoutInfoMap = new HashMap<>();
            do{
                String title = rs.getString("title");
                String info = rs.getString("info");
                workoutInfoMap.put(title, info);
            }while (rs.next());
            workoutInfo.setWorkoutInfo(workoutInfoMap);
        }
        return workoutInfo;
    }
}
