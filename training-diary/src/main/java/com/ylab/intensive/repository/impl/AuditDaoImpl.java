package com.ylab.intensive.repository.impl;

import com.ylab.intensive.repository.AuditDao;
import com.ylab.intensive.repository.extractor.AuditExtractor;
import com.ylab.intensive.repository.extractor.ListExtractor;
import com.ylab.intensive.model.Pageable;
import com.ylab.intensive.model.entity.Audit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation class for {@link AuditDao}.
 */
@Log4j2
@Repository
@RequiredArgsConstructor
public class AuditDaoImpl implements AuditDao {
    /**
     * The JDBC template used for executing SQL queries against the database.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * The audit extractor responsible for extracting audit information
     * from the provided {@code ResultSet}.
     */
    private final AuditExtractor extractor;

    @Override
    public List<Audit> getUserActions(int userId, Pageable pageable) {
        String FIND_ALL_AUDIT = """
                SELECT a.id, a.user_id, a.date_of_action, a.action
                FROM internal.audit a
                WHERE a.user_id = ?
                ORDER BY a.date_of_action
                LIMIT ?
                OFFSET ?
                """;
        int offset = pageable.getPage() * pageable.getCount();
        return jdbcTemplate.query(FIND_ALL_AUDIT, new ListExtractor<>(extractor), userId, pageable.getCount(), offset);
    }

    @Override
    public void insertUserAction(int userId, String action) {
        String SAVE = """
                INSERT INTO internal.audit(user_id, action, date_of_action)
                VALUES (?, ?, CURRENT_TIMESTAMP)
                """;
        jdbcTemplate.update(SAVE, userId, action);
    }
}
