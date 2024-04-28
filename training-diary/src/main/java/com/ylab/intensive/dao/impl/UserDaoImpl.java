package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.util.DaoUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.*;

/**
 * Implementation of the UserDao interface.
 * This class provides methods to interact with user data in the database.
 */
@Log4j2
@ApplicationScoped
@NoArgsConstructor
public class UserDaoImpl implements UserDao {

    @Override
    public User save(User user, int roleId) {
        String INSERT_USER = "INSERT INTO internal.user (uuid, email, password, role_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setObject(1, user.getUuid());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setInt(4, roleId);

                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt("id"));
                } else {
                    throw new DaoException("Creating user failed, no ID obtained.");
                }
            } catch (SQLException exc) {
                connection.rollback();
                DaoUtil.handleSQLException(exc, log);
            }
            connection.commit();
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String FIND_BY_EMAIL = """
                SELECT u.id as user_id, u.uuid, u.email, u.password, u.role_id
                FROM internal.user u
                WHERE u.email = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildUser(resultSet));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateUserRole(UUID uuid, int roleId) {
        String UPDATE_USER_ROLE = "UPDATE internal.user SET role_id = ? WHERE uuid = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);
            int rowsAffected = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_ROLE)) {
                preparedStatement.setInt(1, roleId);
                preparedStatement.setObject(2, uuid);

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
        String FIND_ALL_USERS = "SELECT u.id as user_id, u.uuid, u.email, u.password, u.role_id FROM internal.user u";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                userList.add(buildUser(resultSet));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
            return Collections.emptyList();
        }
        return userList;
    }

    @Override
    public Optional<User> findByUUID(UUID uuid) {
        String FIND_BY_EMAIL = """
                SELECT u.id as user_id, u.uuid, u.email, u.password, u.role_id
                FROM internal.user u
                WHERE u.uuid = ?
                """;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL)) {
            preparedStatement.setObject(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildUser(resultSet));
            }
        } catch (SQLException exc) {
            DaoUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setUuid((UUID) resultSet.getObject("uuid"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setWorkouts(new ArrayList<>());
        user.setAction(new ArrayList<>());
        user.setRole(Role.fromValue(resultSet.getString("role_id")));
        return user;
    }
}
