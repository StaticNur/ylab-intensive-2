package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.util.DaoUtil;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserDao interface.
 * This class provides methods to interact with user data in the database.
 */
@Log4j2
public class UserDaoImpl implements UserDao {

    @Override
    public boolean save(User user, int roleId) {
        String INSERT_USER = "INSERT INTO internal.user (email, password, role_id) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);
            int rowsAffected = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setInt(3, roleId);

                rowsAffected = preparedStatement.executeUpdate();

            } catch (SQLException exc) {
                connection.rollback();
                DaoUtil.handleSQLException(exc, log);
            }

            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
            return false;
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String FIND_BY_EMAIL = """
                SELECT u.id as user_id, u.email, u.password, u.role_id
                FROM internal.user u
                WHERE u.email = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return buildUser(resultSet);
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateUserRole(String email, int roleId) {
        String UPDATE_USER_ROLE = "UPDATE internal.user SET role_id = ? WHERE email = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);
            int rowsAffected = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_ROLE)) {
                preparedStatement.setInt(1, roleId);
                preparedStatement.setString(2, email);

                rowsAffected = preparedStatement.executeUpdate();
            } catch (SQLException exc) {
                connection.rollback();
                DaoUtil.handleSQLException(exc, log);
            }
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
            return false;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String FIND_ALL_USERS = "SELECT u.id as user_id, u.email, u.password, u.role_id FROM internal.user u";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = buildUser(resultSet).orElseThrow(() -> new DaoException("Error in UserDao"));

                userList.add(user);
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
            return Collections.emptyList();
        }
        return userList;
    }

    private Optional<User> buildUser(ResultSet resultSet) throws SQLException {
        Optional<User> user = Optional.of(User.builder().build());
        user.get().setId(resultSet.getInt("user_id"));
        user.get().setEmail(resultSet.getString("email"));
        user.get().setPassword(resultSet.getString("password"));
        user.get().setWorkout(new ArrayList<>());
        user.get().setAction(new ArrayList<>());
        user.get().setRole(Role.fromValue(resultSet.getString("role_id")));
        return user;
    }
}
