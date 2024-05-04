package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutTypeDao;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.WorkoutType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * Implementation class for {@link WorkoutTypeDao}.
 */
@Log4j2
@Repository
@RequiredArgsConstructor
public class WorkoutTypeDaoImpl implements WorkoutTypeDao {

    private final JdbcTemplate jdbcTemplate;

    public WorkoutType saveType(int userId, String type) {
        String INSERT_TYPE = "INSERT INTO internal.workout_type (user_id, type) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_TYPE, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setString(2, type);
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new DaoException("Creating workout type failed, no ID obtained.");
        }

        WorkoutType workoutType = new WorkoutType();
        workoutType.setId(keyHolder.getKey().intValue());
        workoutType.setUserId(userId);
        workoutType.setType(type);
        return workoutType;
    }

    @Override
    public void updateType(int userId, String oldType, String newType) {
        String UPDATE_TYPE = "UPDATE internal.workout_type SET type = ? WHERE user_id = ? AND type = ?";
        int affectedRows = jdbcTemplate.update(UPDATE_TYPE, newType, userId, oldType);

        if (affectedRows == 0) {
            throw new DaoException("Updating workout type failed, no rows affected.");
        }
    }

    @Override
    public List<WorkoutType> findByUserId(int userId) {
        String FIND_BY_USER_ID = "SELECT id, user_id, type FROM internal.workout_type WHERE user_id = ?";
        return jdbcTemplate.query(FIND_BY_USER_ID, new Object[]{userId}, (rs, rowNum) -> buildWorkoutType(rs));
    }

    @Override
    public void delete(int userId) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout_type WHERE user_id = ?";
        jdbcTemplate.update(DELETE_WORKOUT, userId);
    }

    public Optional<WorkoutType> findById(int id) {
        String FIND_BY_ID = "SELECT id, user_id, type FROM internal.workout_type WHERE id = ?";
        List<WorkoutType> workoutTypes = jdbcTemplate.query(FIND_BY_ID, new Object[]{id}, (rs, rowNum) -> buildWorkoutType(rs));
        return workoutTypes.stream().findFirst();
    }

    @Override
    public Optional<WorkoutType> findByType(String typeName) {
        String FIND_BY_TYPE = "SELECT id, user_id, type FROM internal.workout_type WHERE type = ?";
        List<WorkoutType> workoutTypes = jdbcTemplate.query(FIND_BY_TYPE, new Object[]{typeName}, (rs, rowNum) -> buildWorkoutType(rs));
        return workoutTypes.stream().findFirst();
    }

   /* @Override
    public WorkoutType saveType(int userId, String type) {
        String INSERT_TYPE = "INSERT INTO internal.workout_type (user_id, type) VALUES (?, ?)";
        WorkoutType workoutType = new WorkoutType();
        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TYPE, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, type);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Creating workout type failed, no rows affected.");
                }

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    workoutType.setId(generatedKeys.getInt("id"));
                    workoutType.setUserId(userId);
                    workoutType.setType(type);
                } else {
                    throw new DaoException("Creating workout type failed, no ID obtained.");
                }
            } catch (SQLException exc) {
                connection.rollback();
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return workoutType;
    }

    @Override
    public void updateType(int userId, String oldType, String newType) {
        String UPDATE_TYPE = "UPDATE internal.workout_type SET type = ? WHERE user_id = ? AND type = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TYPE)) {
                preparedStatement.setString(1, newType);
                preparedStatement.setInt(2, userId);
                preparedStatement.setString(3, oldType);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DaoException("Updating workout type failed, no rows affected.");
                }
            } catch (SQLException exc) {
                connection.rollback();
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
    }

    @Override
    public List<WorkoutType> findByUserId(int userId) {
        List<WorkoutType> types = new ArrayList<>();
        String FIND_BY_USER_ID = "SELECT id, user_id, type FROM internal.workout_type WHERE user_id = ?";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USER_ID)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                types.add(buildWorkoutType(resultSet));
            }
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return types;
    }

    @Override
    public void delete(int user_id) {
        String DELETE_WORKOUT = "DELETE FROM internal.workout_type WHERE user_id = ?";

        try (Connection connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKOUT)) {
                preparedStatement.setInt(1, user_id);

                preparedStatement.executeUpdate();
            } catch (SQLException exc) {
                connection.rollback();
                SQLExceptionUtil.handleSQLException(exc, log);
            }

            connection.commit();
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
    }

    @Override
    public Optional<WorkoutType> findById(int id) {
        String FIND_BY_ID = "SELECT id, user_id, type FROM internal.workout_type WHERE id = ?";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildWorkoutType(resultSet));
            }
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }

    @Override
    public Optional<WorkoutType> findByType(String typeName) {
        String FIND_BY_ID = "SELECT id, user_id, type FROM internal.workout_type WHERE type = ?";

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setString(1, typeName);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildWorkoutType(resultSet));
            }
        } catch (SQLException exc) {
            SQLExceptionUtil.handleSQLException(exc, log);
        }
        return Optional.empty();
    }*/

    private WorkoutType buildWorkoutType(ResultSet resultSet) throws SQLException {
        WorkoutType workoutType = new WorkoutType();
        workoutType.setId(resultSet.getInt("id"));
        workoutType.setUserId(resultSet.getInt("user_id"));
        workoutType.setType(resultSet.getString("type"));
        return workoutType;
    }
}
