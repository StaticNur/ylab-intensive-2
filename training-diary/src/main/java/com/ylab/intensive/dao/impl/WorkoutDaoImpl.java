package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.model.User;
import com.ylab.intensive.model.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDaoImpl implements WorkoutDao {
    private final List<Workout> userDB = new ArrayList<>();
}
