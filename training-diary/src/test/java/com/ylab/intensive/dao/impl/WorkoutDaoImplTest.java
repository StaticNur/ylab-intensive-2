package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.container.PostgresTestContainer;
import com.ylab.intensive.dao.container.TestConfigurationEnvironment;
import com.ylab.intensive.model.entity.Workout;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Workout Database Operations Testing")
class WorkoutDaoImplTest extends TestConfigurationEnvironment {

    private static WorkoutDaoImpl workoutDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        workoutDao = new WorkoutDaoImpl();
    }

    @Test
    @DisplayName("Find workout by date - workout exists")
    void testFindByDate_WorkoutExists() {
        LocalDate date = LocalDate.of(2024, 4, 17);

        Optional<Workout> workout = workoutDao.findByDate(date, 1);

        assertThat(workout).isPresent();
    }

    @Test
    @DisplayName("Find workout by date - workout does not exist")
    void testFindByDate_WorkoutDoesNotExist() {
        LocalDate date = LocalDate.of(1000, 1, 1);

        Optional<Workout> workout = workoutDao.findByDate(date, 1);

        assertThat(workout).isEmpty();
    }

    @Test
    @DisplayName("Save workout - success")
    void testSaveWorkout_Success() {
        Workout workout = new Workout();
        workout.setUserId(1);
        workout.setDate(LocalDate.now());
        workout.setDuration(Duration.ofMinutes(30));
        workout.setCalorie(300.0f);

        Workout savedWorkout = workoutDao.saveWorkout(workout);

        assertThat(savedWorkout.getId()).isPositive();
    }

    @Test
    @DisplayName("Find workouts by user ID")
    void testFindByUserId() {
        int userId = 1;

        List<Workout> workouts = workoutDao.findByUserId(userId);

        assertThat(workouts).isNotEmpty();
    }

    @Test
    @DisplayName("Delete workout - success")
    void testDeleteWorkout_Success() {
        LocalDate date = LocalDate.of(2022, 2, 22);

        workoutDao.deleteWorkout(date, 1);

        Optional<Workout> deletedWorkout = workoutDao.findByDate(date, 1);

        assertThat(deletedWorkout).isEmpty();
    }

    @Test
    @DisplayName("Update calorie for workout - success")
    void testUpdateCalorie_Success() {
        int workoutId = 2;
        float calorie = 400.0f;

        workoutDao.updateCalorie(workoutId, calorie);

        Optional<Workout> updatedWorkout = workoutDao.findByDate(LocalDate.parse("2024-04-19"), 2);

        assertThat(updatedWorkout)
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("calorie", calorie);
    }

    @Test
    @DisplayName("Update duration for workout - success")
    void testUpdateDuration_Success() {
        int workoutId = 2;
        Duration duration = Duration.ofMinutes(45);

        workoutDao.updateDuration(workoutId, duration);

        Optional<Workout> updatedWorkout = workoutDao.findByDate(LocalDate.parse("2024-04-19"), 2);

        assertThat(updatedWorkout)
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("duration", duration);
    }

    @Test
    @DisplayName("Find workouts by duration")
    void testFindByDuration() {
        LocalDate begin = LocalDate.of(1000, 1, 1);
        LocalDate end = LocalDate.of(3000, 1, 1);

        List<Workout> workouts = workoutDao.findByDuration( 1,begin, end);

        assertThat(workouts).isNotEmpty();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}