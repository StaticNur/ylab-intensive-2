package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.RoleDao;
import com.ylab.intensive.model.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementation class for {@link RoleDao}.
 */
@Log4j2
@Repository
@RequiredArgsConstructor
public class RoleDaoImpl implements RoleDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int findByName(Role role) {
        String FIND_BY_NAME = """
                SELECT r.id
                FROM internal.role r
                WHERE r.name = ?
                """;
        return jdbcTemplate.queryForObject(FIND_BY_NAME, Integer.class, role);

        /*try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME)) {
            preparedStatement.setString(1, role.name());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new DaoException("Role not found.");
            }
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
            return -1;
        }*/
    }
}
