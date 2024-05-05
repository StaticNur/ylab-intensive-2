package com.ylab.intensive.repository.extractor;

import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The {@code UserExtractor} class is an implementation of the
 * {@code ResultSetExtractor<User>} interface for extracting
 * a {@code User} object from a {@code ResultSet}.
 */
@Component
public class UserExtractor implements ResultSetExtractor<User> {

    /**
     * Extracts a {@code User} object from the provided {@code ResultSet}.
     *
     * @param rs The {@code ResultSet} containing the data.
     * @return A {@code User} object extracted from the {@code ResultSet},
     * or {@code null} if no data is present in the result set.
     * @throws SQLException If a database access error occurs or the column
     *                      index is out of range.
     */
    @Override
    public User extractData(ResultSet rs) throws SQLException {
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getInt("user_id"));
            user.setUuid((UUID) rs.getObject("uuid"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setWorkouts(new ArrayList<>());
            user.setAction(new ArrayList<>());
            user.setRole(Role.fromValue(rs.getString("role_id")));
        }
        return user;
    }
}
