package com.ylab.intensive.model.mapper;

import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Endpoints;
import com.ylab.intensive.model.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class ResultSetMapper {
    public Optional<User> buildUser(ResultSet resultSet) throws SQLException {
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
