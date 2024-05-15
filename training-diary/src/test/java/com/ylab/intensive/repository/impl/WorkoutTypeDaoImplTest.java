package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.repository.WorkoutTypeDao;
import com.ylab.intensive.repository.config.TestRepositories;
import com.ylab.intensive.tag.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertThrows;

@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тесты для реализации WorkoutTypeDao")
class WorkoutTypeDaoImplTest {

    @Autowired
    private WorkoutTypeDao workoutTypeDao;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен сохранять тип тренировки в базе данных")
        void shouldSaveWorkoutType() {
            int userId = 1;
            String type = "Test Type";

            WorkoutType savedType = workoutTypeDao.saveType(userId, type);

            assertThat(savedType.getId()).isPositive();
            assertThat(savedType.getUserId()).isEqualTo(userId);
            assertThat(savedType.getType()).isEqualTo(type);
        }

        @Test
        @DisplayName("Должен обновлять тип тренировки в базе данных")
        void shouldUpdateWorkoutType() {
            int userId = 1;
            String oldType = "cardio";
            String newType = "New Type";

            workoutTypeDao.updateType(userId, oldType, newType);

            assertThat(workoutTypeDao.findTypeByUserId(userId, newType))
                    .isPresent()
                    .hasValueSatisfying(updatedType -> {
                        assertThat(updatedType.getUserId()).isEqualTo(userId);
                        assertThat(updatedType.getType()).isEqualTo(newType);
                    });
        }

        @Test
        @DisplayName("Должен находить типы тренировок по идентификатору пользователя")
        void shouldFindWorkoutTypesByUserId() {
            int userId = 1;

            assertThat(workoutTypeDao.findByUserId(userId))
                    .extracting("type")
                    .contains("yoga");
        }

        @Test
        @DisplayName("Должен удалять тип тренировки из базы данных")
        void shouldDeleteWorkoutType() {
            int userId = 2;

            workoutTypeDao.delete(userId);

            Assertions.assertThat(workoutTypeDao.findByUserId(userId))
                    .isEmpty();
        }

        @Test
        @DisplayName("Должен находить тип тренировки по имени")
        void shouldFindWorkoutTypeByName() {
            String type = "Test Type";
            int userId = 1;

            workoutTypeDao.saveType(userId, type);

            assertThat(workoutTypeDao.findByName(type))
                    .isPresent()
                    .hasValueSatisfying(foundType -> {
                        assertThat(foundType.getType()).isEqualTo(type);
                        assertThat(foundType.getUserId()).isEqualTo(userId);
                    });
        }

        @Test
        @DisplayName("Должен возвращать пустой Optional если тип тренировки не найден по имени")
        void shouldReturnEmptyOptionalIfWorkoutTypeNotFoundByName() {
            String type = "Nonexistent Type";

            assertThat(workoutTypeDao.findByName(type))
                    .isEmpty();
        }

        @Test
        @DisplayName("Должен находить тип тренировки по идентификатору пользователя и имени")
        void shouldFindWorkoutTypeByUserIdAndName() {
            int userId = 1;
            String type = "yoga";

            workoutTypeDao.saveType(userId, type);

            assertThat(workoutTypeDao.findTypeByUserId(userId, type))
                    .isPresent()
                    .hasValueSatisfying(foundType -> {
                        assertThat(foundType.getType()).isEqualTo(type);
                        assertThat(foundType.getUserId()).isEqualTo(userId);
                    });
        }

        @Test
        @DisplayName("Должен возвращать пустой Optional если тип тренировки не найден по идентификатору пользователя и имени")
        void shouldReturnEmptyOptionalIfWorkoutTypeNotFoundByUserIdAndName() {
            int userId = 2;
            String type = "Nonexistent Type";

            assertThat(workoutTypeDao.findTypeByUserId(userId, type))
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Не должен обновлять тип тренировки, если пользователь не существует")
        void shouldNotUpdateWorkoutTypeIfUserDoesNotExist() {
            int nonExistentUserId = -1;
            String oldType = "cardio";
            String newType = "New Type";

            workoutTypeDao.updateType(nonExistentUserId, oldType, newType);

            assertThat(workoutTypeDao.findTypeByUserId(nonExistentUserId, newType))
                    .isEmpty();
        }

        @Test
        @DisplayName("Не должен удалять тип тренировки, если пользователь не существует")
        void shouldNotDeleteWorkoutTypeIfUserDoesNotExist() {
            int nonExistentUserId = -1;

            workoutTypeDao.delete(nonExistentUserId);

            assertThat(workoutTypeDao.findByUserId(nonExistentUserId))
                    .isEmpty();
        }

        @Test
        @DisplayName("Не должен сохранять тип тренировки, если пользователь не существует")
        void shouldNotSaveWorkoutTypeIfUserDoesNotExist() {
            int nonExistentUserId = -1;
            String type = "Test Type";

            assertThrows(DataIntegrityViolationException.class, () -> {
                workoutTypeDao.saveType(nonExistentUserId, type);
            });
        }

        @Test
        @DisplayName("Не должен находить тип тренировки по имени, если он не существует")
        void shouldNotFindWorkoutTypeByNameIfDoesNotExist() {
            String nonExistentType = "Nonexistent Type";

            assertThat(workoutTypeDao.findByName(nonExistentType))
                    .isEmpty();
        }

    }
}