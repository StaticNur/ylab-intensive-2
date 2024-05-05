package com.ylab.intensive.repository.impl;

import com.ylab.intensive.repository.UserDao;
import com.ylab.intensive.repository.extractor.ListExtractor;
import com.ylab.intensive.repository.extractor.UserExtractor;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

/**
 * Implementation of the UserDao interface.
 * This class provides methods to interact with user data in the database.
 */
@Log4j2
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserExtractor extractor;

    @Override
    public User save(User user, int roleId) {
        String INSERT_USER = """
                INSERT INTO internal.user (uuid, email, password, role_id) VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getUuid());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, roleId);
            return ps;
        }, keyHolder);

        if ((keyHolder.getKeys() != null) && !keyHolder.getKeys().isEmpty()) {
            Map<String, Object> keys = keyHolder.getKeys();
            Number id = (Number) keys.get("id");
            user.setId(id.intValue());
        } else {
            throw new DaoException("Creating user failed, no ID obtained.");
        }
        user.setWorkouts(new ArrayList<>());
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String FIND_BY_EMAIL = """
                SELECT u.id as user_id, u.uuid, u.email, u.password, u.role_id
                FROM internal.user u
                WHERE u.email = ?
                """;
        User users = jdbcTemplate.query(FIND_BY_EMAIL, extractor, email);
        return Optional.ofNullable(users);
    }

    @Override
    public Optional<User> findByUUID(UUID uuid) {
        String FIND_BY_UUID = """
                SELECT u.id as user_id, u.uuid, u.email, u.password, u.role_id
                FROM internal.user u
                WHERE u.uuid = ?
                """;
        User users = jdbcTemplate.query(FIND_BY_UUID, extractor, uuid);
        return Optional.ofNullable(users);
    }

    @Override
    public boolean updateUserRole(UUID uuid, int roleId) {
        String UPDATE_USER_ROLE = """
                UPDATE internal.user SET role_id = ? WHERE uuid = ?
                """;
        int rowsAffected = jdbcTemplate.update(UPDATE_USER_ROLE, roleId, uuid);
        return rowsAffected > 0;
    }

    @Override
    public List<User> findAll() {
        String FIND_ALL_USERS = """
                SELECT u.id as user_id, u.uuid, u.email, u.password, u.role_id FROM internal.user u
                """;
        return jdbcTemplate.query(FIND_ALL_USERS, new ListExtractor<>(extractor));
    }
}
