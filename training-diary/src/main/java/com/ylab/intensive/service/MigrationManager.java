package com.ylab.intensive.service;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.ui.AnsiColor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

/**
 * MigrationManager Class.
 * This class is responsible for migrating data during application startup.
 * It adds some initial users and workouts to the user database.
 */
public class MigrationManager {
    /**
     * User DAO.
     * This DAO is responsible for data access operations related to users.
     */
    @Inject
    private UserDao userDao;

    /**
     * Output data.
     * Allows outputting data to the user.
     */
    @Inject
    private OutputData outputData;

    /**
     * ANSI color.
     * Allows setting the color for console output.
     */
    @Inject
    private AnsiColor ansiColor;

    public void migrate() {
        List<User> userDB = userDao.findAll();
        User user1 = new User(1, "user1@example.com", "password1", new ArrayList<>(), new ArrayList<>(), Role.USER);
        User user2 = new User(2, "user2@example.com", "password2", new ArrayList<>(), new ArrayList<>(), Role.ADMIN);
        userDB.add(user1);
        userDB.add(user2);


        Workout workout1 = new Workout(1, LocalDate.now(), new HashSet<>(Arrays.asList("running", "cycling")), Duration.ofHours(1), 300f, new HashMap<>());
        workout1.getInfo().put("количество выполненных упражнений", "20 - подтягиваний и 100 - отжиманий");
        workout1.getInfo().put("пройденное расстояние", "12,34 км");
        userDB.get(0).getWorkout().add(workout1);
        Workout workout2 = new Workout(1, LocalDate.now(), new HashSet<>(Arrays.asList("running", "cycling")), Duration.ofHours(1), 300f, new HashMap<>());
        workout2.getInfo().put("поднятия штанги", "20 - раз");
        userDB.get(0).getWorkout().add(workout2);

        Workout workout3 = new Workout(2, LocalDate.now(), new HashSet<>(Collections.singletonList("swimming")), Duration.ofMinutes(45), 200f, new HashMap<>());
        workout3.getInfo().put("проплавленное расстояние", "450 метров");
        userDB.get(1).getWorkout().add(workout3);
        outputData.output(ansiColor.yellowText("Миграция данных успешно завершена!"));
    }
}