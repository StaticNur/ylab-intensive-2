package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.model.mapper.ResultSetMapper;
import com.ylab.intensive.service.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserDao interface.
 * This class provides methods to interact with user data in the database.
 */
public class UserDaoImpl implements UserDao {

    @Inject
    private ResultSetMapper resultSetMapper;

    @Override
    public boolean save(User user, int roleId) {
        String INSERT_USER = "INSERT INTO internal.user (email, password, role_id) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);
            int rowsAffected = 0;

            try (PreparedStatement userStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, user.getEmail());
                userStatement.setString(2, user.getPassword());
                userStatement.setInt(3, roleId);

                rowsAffected = userStatement.executeUpdate();

            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }

            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
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
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSetMapper.buildUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return Optional.empty();
    }


    /**
     * Updates the user's role in the database by the specified identifier.
     *
     * @param email the user email
     * @return the updated user
     * @throws DaoException if an SQL exception occurs
     */
    @Override
    public boolean updateUserRole(String email, int roleId) {
        String UPDATE_USER_ROLE = "UPDATE internal.user SET role_id = ? WHERE email = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);
            int rowsAffected = 0;

            try (PreparedStatement updateUserRoleStatement = connection.prepareStatement(UPDATE_USER_ROLE)) {
                updateUserRoleStatement.setInt(1, roleId);
                updateUserRoleStatement.setString(2, email);

                rowsAffected = updateUserRoleStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e.getMessage());
            }
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String FIND_ALL_USERS = "SELECT u.id as user_id, u.email, u.password, u.role_id FROM internal.user u";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_USERS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = resultSetMapper.buildUser(resultSet).orElseThrow(() -> new DaoException("Error in UserDao"));

                userList.add(user);
            }
        } catch (SQLException | DaoException e) {
            throw new DaoException("Error fetching users. " + e.getMessage());
        }
        return userList;
    }

}
