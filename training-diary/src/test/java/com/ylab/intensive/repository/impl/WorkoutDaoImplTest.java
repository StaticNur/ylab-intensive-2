package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.repository.WorkoutDao;
import com.ylab.intensive.repository.config.TestRepositories;
import com.ylab.intensive.tag.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тесты для реализации WorkoutDao")
class WorkoutDaoImplTest {

    @Autowired
    private WorkoutDao workoutDao;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен находить тренировку по дате и идентификатору пользователя")
        void shouldFindWorkoutByDateAndUserId() {
            LocalDate date = LocalDate.parse("2024-04-17");
            int userId = 1;

            assertThat(workoutDao.findByDate(date, userId))
                    .isPresent()
                    .hasValueSatisfying(workout -> {
                        assertThat(workout.getDate()).isEqualTo(date);
                        assertThat(workout.getUserId()).isEqualTo(userId);
                    });
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

            Assertions.assertThat(workouts).isNotEmpty();
            workouts.forEach(workout -> assertThat(workout.getUserId()).isEqualTo(userId));
        }

        @Test
        @DisplayName("Должен удалять тренировку")
        void shouldDeleteWorkout() {
            int userId = 2;
            int workoutId = 3;

            workoutDao.deleteWorkout(userId, workoutId);

            assertThat(workoutDao.findByDate(LocalDate.parse("2024-03-19"), userId))
                    .isNotPresent();
        }

        @Test
        @DisplayName("Должен обновлять количество калорий для тренировки")
        void shouldUpdateCalorieForWorkout() {
            int workoutId = 1;
            float newCalorie = 600.0f;

            workoutDao.updateCalorie(workoutId, newCalorie);

            assertThat(workoutDao.findByUUID(UUID.fromString("123e4567-e89b-12d3-a456-426614174002")))
                    .isPresent()
                    .hasValueSatisfying(workout -> {
                        assertThat(workout.getCalorie()).isEqualTo(newCalorie);
                    });
        }

        @Test
        @DisplayName("Должен обновлять продолжительность тренировки")
        void shouldUpdateDurationForWorkout() {
            int workoutId = 4;
            Duration newDuration = Duration.ofMinutes(90);

            workoutDao.updateDuration(workoutId, newDuration);

            assertThat(workoutDao.findByDate(LocalDate.parse("2022-02-22"), 1))
                    .isPresent()
                    .hasValueSatisfying(workout -> {
                        assertThat(workout.getDuration()).isEqualTo(newDuration);
                    });
        }

        @Test
        @DisplayName("Должен находить тренировки по диапазону дат и идентификатору пользователя")
        void shouldFindWorkoutsByDateRangeAndUserId() {
            int userId = 1;
            LocalDate startDate = LocalDate.of(2000, 6, 1);
            LocalDate endDate = LocalDate.of(2025, 6, 10);

            List<Workout> workouts = workoutDao.findByDuration(userId, startDate, endDate);

            Assertions.assertThat(workouts).isNotEmpty();
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

            assertThat(workoutDao.findByUUID(workoutUUID))
                    .isPresent()
                    .hasValueSatisfying(workout -> {
                        assertThat(workout.getUuid()).isEqualTo(workoutUUID);
                    });
        }

        @Test
        @DisplayName("Должен обновлять тип тренировки")
        void shouldUpdateWorkoutType() {
            int workoutId = 4;
            String newType = "Cycling";

            workoutDao.updateType(workoutId, newType);

            assertThat(workoutDao.findByDate(LocalDate.parse("2022-02-22"), 1))
                    .isPresent()
                    .hasValueSatisfying(workout -> {
                        assertThat(workout.getType()).isEqualTo(newType);
                    });
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Не должен находить тренировку по дате и идентификатору пользователя, если она не существует")
        void shouldNotFindWorkoutByDateAndUserIdIfDoesNotExist() {
            LocalDate date = LocalDate.parse("2024-01-01");
            int userId = -1;

            assertThat(workoutDao.findByDate(date, userId))
                    .isNotPresent();
        }

        @Test
        @DisplayName("Не должен находить тренировки пользователя в заданном диапазоне дат, если их нет")
        void shouldNotFindWorkoutsByDateRangeAndUserIdIfNoneExist() {
            int userId = 1;
            LocalDate startDate = LocalDate.of(2025, 1, 1);
            LocalDate endDate = LocalDate.of(2025, 12, 31);

            Assertions.assertThat(workoutDao.findByDuration(userId, startDate, endDate))
                    .isEmpty();
        }

        @Test
        @DisplayName("Не должен находить тренировку по несуществующему UUID")
        void shouldNotFindWorkoutByNonExistentUUID() {
            UUID nonExistentUUID = UUID.randomUUID();

            assertThat(workoutDao.findByUUID(nonExistentUUID))
                    .isNotPresent();
        }

        @Test
        @DisplayName("Не должен обновлять тип тренировки для несуществующей тренировки")
        void shouldNotUpdateWorkoutTypeForNonExistentWorkout() {
            int nonExistentWorkoutId = -1;
            String newType = "Cycling";

            workoutDao.updateType(nonExistentWorkoutId, newType);

            assertThat(workoutDao.findByDate(LocalDate.parse("2022-02-22"), 1))
                    .isNotEqualTo("Cycling");
        }
    }
}