package com.ylab.intensive.repository.extractor;

import com.ylab.intensive.model.entity.Audit;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The {@code AuditExtractor} class is an implementation of the
 * {@code ResultSetExtractor<Audit>} interface for extracting
 * a {@code Audit} object from a {@code ResultSet}.
 */
@Component
public class AuditExtractor implements ResultSetExtractor<Audit> {

    /**
     * Extracts a {@code Audit} object from the provided {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} containing the data.
     * @return A {@code Audit} object extracted from the {@code ResultSet},
     * or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public Audit extractData(ResultSet rs) throws SQLException {
        Audit audit = null;
        if (rs.next()) {
            audit = new Audit(rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("date_of_action").toLocalDateTime(),
                    rs.getString("action"));
        }
        return audit;
    }
}
