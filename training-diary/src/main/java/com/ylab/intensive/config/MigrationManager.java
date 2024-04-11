package com.ylab.intensive.config;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.dao.impl.UserDaoImpl;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.model.User;
import com.ylab.intensive.model.Workout;
import com.ylab.intensive.model.enums.Role;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class MigrationManager {
    @Inject
    private UserDao userDao;
    public void migrate() {
        List<User> userDB = userDao.getUserDB();
        User user1 = new User(1, "user1@example.com", "password1", new ArrayList<>(), new ArrayList<>(), Role.USER);
        User user2 = new User(2, "user2@example.com", "password2", new ArrayList<>(), new ArrayList<>(), Role.ADMIN);
        userDB.add(user1);
        userDB.add(user2);


        Workout workout1 = new Workout(1, LocalDate.now(), new HashSet<>(Arrays.asList("running", "cycling")), Duration.ofHours(1), 300f, new HashMap<>());
        workout1.getInfo().put("количество выполненных упражнений", "в сумме - 20 из низ 10 - подтягиваний и 10 - отжиманий");
        workout1.getInfo().put("пройденное расстояние", "12,34 км");
        userDB.get(0).getWorkout().add(workout1);

        Workout workout2 = new Workout(2, LocalDate.now(), new HashSet<>(Collections.singletonList("swimming")), Duration.ofMinutes(45), 200f, new HashMap<>());
        workout2.getInfo().put("проплавленное расстояние", "450 метров");
        userDB.get(1).getWorkout().add(workout2);
    }
}
