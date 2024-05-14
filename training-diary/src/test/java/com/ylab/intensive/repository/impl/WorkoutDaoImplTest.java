package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.repository.WorkoutDao;
import com.ylab.intensive.repository.container.PostgresTestContainer;
import com.ylab.intensive.repository.container.TestConfigurationEnvironment;
import com.ylab.intensive.repository.extractor.WorkoutExtractor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты для реализации WorkoutDaoImpl")
public class WorkoutDaoImplTest extends TestConfigurationEnvironment {

    private static WorkoutDao workoutDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        JdbcTemplate jdbcTemplate = PostgresTestContainer.getJdbcTemplate();
        workoutDao = new WorkoutDaoImpl(jdbcTemplate, new WorkoutExtractor());
    }

    @Test
    @DisplayName("Должен находить тренировку по дате и идентификатору пользователя")
    void shouldFindWorkoutByDateAndUserId() {
        LocalDate date = LocalDate.parse("2024-04-17");
        int userId = 1;

        Optional<Workout> workoutOptional = workoutDao.findByDate(date, userId);

        assertThat(workoutOptional).isPresent();
        Workout workout = workoutOptional.get();
        assertThat(workout.getDate()).isEqualTo(date);
        assertThat(workout.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Должен сохранять новую тренировку")
    void shouldSaveWorkout() {
        LocalDate localDate = LocalDate.parse("2024-04-17");
        Workout workout = new Workout();
        workout.setUserId(1);
        workout.setUuid(UUID.randomUUID());
        workout.setType("Running");
        workout.setDate(localDate);
        workout.setDuration(Duration.ofHours(1));
        workout.setCalorie(500.0f);

        Workout savedWorkout = workoutDao.saveWorkout(workout);

        assertThat(savedWorkout.getId()).isPositive();
        assertThat(savedWorkout.getUuid()).isNotNull();
        assertThat(savedWorkout.getType()).isEqualTo("Running");
        assertThat(savedWorkout.getDate()).isEqualTo(localDate);
        assertThat(savedWorkout.getDuration()).isEqualTo(Duration.ofHours(1));
        assertThat(savedWorkout.getCalorie()).isEqualTo(500.0f);
    }

    @Test
    @DisplayName("Должен находить все тренировки пользователя")
    void shouldFindWorkoutsByUserId() {
        int userId = 1;

        List<Workout> workouts = workoutDao.findByUserId(userId);

        assertThat(workouts).isNotEmpty();
        workouts.forEach(workout -> assertThat(workout.getUserId()).isEqualTo(userId));
    }

    @Test
    @DisplayName("Должен удалять тренировку")
    void shouldDeleteWorkout() {
        int userId = 2;
        int workoutId = 3;

        workoutDao.deleteWorkout(userId, workoutId);

        Optional<Workout> workoutOptional = workoutDao.findByDate(LocalDate.parse("2024-03-19"), userId);
        assertThat(workoutOptional).isNotPresent();
    }

    @Test
    @DisplayName("Должен обновлять количество калорий для тренировки")
    void shouldUpdateCalorieForWorkout() {
        int workoutId = 1;
        float newCalorie = 600.0f;

        workoutDao.updateCalorie(workoutId, newCalorie);

        Optional<Workout> workoutOptional = workoutDao.findByUUID(UUID.fromString("123e4567-e89b-12d3-a456-426614174002"));
        assertThat(workoutOptional).isPresent();
        Workout workout = workoutOptional.get();
        assertThat(workout.getCalorie()).isEqualTo(newCalorie);
    }
    @Test
    @DisplayName("Должен обновлять продолжительность тренировки")
    void shouldUpdateDurationForWorkout() {
        int workoutId = 4;
        Duration newDuration = Duration.ofMinutes(90);

        workoutDao.updateDuration(workoutId, newDuration);

        Optional<Workout> workoutOptional = workoutDao.findByDate(LocalDate.parse("2022-02-22"), 1);
        assertThat(workoutOptional).isPresent();
        Workout workout = workoutOptional.get();
        assertThat(workout.getDuration()).isEqualTo(newDuration);
    }

    @Test
    @DisplayName("Должен находить тренировки по диапазону дат и идентификатору пользователя")
    void shouldFindWorkoutsByDateRangeAndUserId() {
        int userId = 1;
        LocalDate startDate = LocalDate.of(2000, 6, 1);
        LocalDate endDate = LocalDate.of(2025, 6, 10);

        List<Workout> workouts = workoutDao.findByDuration(userId, startDate, endDate);

        assertThat(workouts).isNotEmpty();
        workouts.forEach(workout -> {
            assertThat(workout.getUserId()).isEqualTo(userId);
            assertThat(workout.getDate()).isBetween(startDate, endDate);
        });
    }

    @Test
    @DisplayName("Должен находить тренировку по UUID")
    void shouldFindWorkoutByUUID() {
        UUID workoutUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174005");

        Optional<Workout> workoutOptional = workoutDao.findByUUID(workoutUUID);

        assertThat(workoutOptional).isPresent();
        Workout workout = workoutOptional.get();
        assertThat(workout.getUuid()).isEqualTo(workoutUUID);
    }

    @Test
    @DisplayName("Должен обновлять тип тренировки")
    void shouldUpdateWorkoutType() {
        int workoutId = 4;
        String newType = "Cycling";

        workoutDao.updateType(workoutId, newType);

        Optional<Workout> workoutOptional = workoutDao.findByDate(LocalDate.parse("2022-02-22"), 1);
        assertThat(workoutOptional).isPresent();
        Workout workout = workoutOptional.get();
        assertThat(workout.getType()).isEqualTo(newType);
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
