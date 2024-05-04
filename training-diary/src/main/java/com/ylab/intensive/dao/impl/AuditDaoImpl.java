package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.AuditDao;
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

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Audit> getUserActions(int userId, Pageable pageable) {
        String FIND_ALL_AUDIT = """
            SELECT a.id, a.user_id, a.date_of_action, a.action
            FROM internal.audit a
            WHERE a.user_id = ?
            ORDER BY a.id
            LIMIT ? OFFSET ?
            """;

        int offset = pageable.getPage() * pageable.getCount();
        List<Audit> actions = jdbcTemplate.query(FIND_ALL_AUDIT,
                new Object[]{userId, pageable.getCount(), offset},
                (rs, rowNum) -> new Audit(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("date_of_action").toLocalDateTime(),
                        rs.getString("action")
                ));

        return actions;
    }

    @Override
    public void insertUserAction(int userId, String action) {
        String SAVE = """
            INSERT INTO internal.audit(user_id, action, date_of_action)
            VALUES (?, ?, CURRENT_TIMESTAMP)
            """;

        jdbcTemplate.update(SAVE, userId, action);
    }

    /*@Override
    public List<Audit> getUserActions(int userId, Pageable pageable) {
        List<Audit> actions = new ArrayList<>();
        int offset = pageable.getPage() * pageable.getCount();
        String FIND_ALL_AUDIT = """
                    SELECT a.id, a.user_id, a.date_of_action, a.action
                    FROM internal.audit a
                    WHERE a.user_id = ?
                    ORDER BY a.id
                    LIMIT ? OFFSET ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_AUDIT)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, pageable.getCount());
            preparedStatement.setInt(3, offset);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LocalDateTime dateOfSubmission = resultSet.getTimestamp("date_of_action").toLocalDateTime();
                Audit audit = new Audit(resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        dateOfSubmission,
                        resultSet.getString("action"));
                actions.add(audit);
            }
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return actions;
    }

    @Override
    public void insertUserAction(int userId, String action) {
        String SAVE = """
                insert into internal.audit(user_id, action, date_of_action) values (?,?,CURRENT_TIMESTAMP)
                """;
        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, action);

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
}
