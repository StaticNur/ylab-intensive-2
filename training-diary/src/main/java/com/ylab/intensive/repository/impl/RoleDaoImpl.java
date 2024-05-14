package com.ylab.intensive.repository.impl;

import com.ylab.intensive.repository.RoleDao;
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
    /**
     * The JDBC template used for executing SQL queries against the database.
     */
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer findByName(Role role) {
        String FIND_BY_NAME = """
                SELECT r.id
                FROM internal.role r
                WHERE r.name = ?
                """;
        return jdbcTemplate.queryForObject(FIND_BY_NAME, Integer.class, role.name());
    }
}
