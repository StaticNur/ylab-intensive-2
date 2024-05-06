package com.ylab.intensive.repository.impl;

import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.repository.container.PostgresTestContainer;
import com.ylab.intensive.repository.container.TestConfigurationEnvironment;
import com.ylab.intensive.repository.extractor.WorkoutTypeExtractor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты для реализации WorkoutTypeDaoImpl")
public class WorkoutTypeDaoImplTest extends TestConfigurationEnvironment {

    private static WorkoutTypeDaoImpl workoutTypeDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        JdbcTemplate jdbcTemplate = PostgresTestContainer.getJdbcTemplate();
        workoutTypeDao = new WorkoutTypeDaoImpl(jdbcTemplate, new WorkoutTypeExtractor());
    }

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

        Optional<WorkoutType> updatedTypeOptional = workoutTypeDao.findTypeByUserId(userId, newType);
        assertThat(updatedTypeOptional).isPresent();
        WorkoutType updatedType = updatedTypeOptional.get();
        assertThat(updatedType.getUserId()).isEqualTo(userId);
        assertThat(updatedType.getType()).isEqualTo(newType);
    }

    @Test
    @DisplayName("Должен находить типы тренировок по идентификатору пользователя")
    void shouldFindWorkoutTypesByUserId() {
        int userId = 1;

        List<WorkoutType> foundTypes = workoutTypeDao.findByUserId(userId);

        assertThat(foundTypes).extracting("type").contains("yoga");
    }

    @Test
    @DisplayName("Должен удалять тип тренировки из базы данных")
    void shouldDeleteWorkoutType() {
        int userId = 2;

        workoutTypeDao.delete(userId);

        List<WorkoutType> deletedTypes = workoutTypeDao.findByUserId(userId);

        assertThat(deletedTypes).isEmpty();
    }

    @Test
    @DisplayName("Должен находить тип тренировки по имени")
    void shouldFindWorkoutTypeByName() {
        String type = "Test Type";
        int userId = 1;

        workoutTypeDao.saveType(userId, type);

        Optional<WorkoutType> foundTypeOptional = workoutTypeDao.findByName(type);
        assertThat(foundTypeOptional).isPresent();
        WorkoutType foundType = foundTypeOptional.get();
        assertThat(foundType.getType()).isEqualTo(type);
        assertThat(foundType.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Должен возвращать пустой Optional если тип тренировки не найден по имени")
    void shouldReturnEmptyOptionalIfWorkoutTypeNotFoundByName() {
        String type = "Nonexistent Type";

        Optional<WorkoutType> foundTypeOptional = workoutTypeDao.findByName(type);
        assertThat(foundTypeOptional).isEmpty();
    }

    @Test
    @DisplayName("Должен находить тип тренировки по идентификатору пользователя и имени")
    void shouldFindWorkoutTypeByUserIdAndName() {
        int userId = 1;
        String type = "yoga";

        workoutTypeDao.saveType(userId, type);

        Optional<WorkoutType> foundTypeOptional = workoutTypeDao.findTypeByUserId(userId, type);
        assertThat(foundTypeOptional).isPresent();
        WorkoutType foundType = foundTypeOptional.get();
        assertThat(foundType.getType()).isEqualTo(type);
        assertThat(foundType.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Должен возвращать пустой Optional если тип тренировки не найден по идентификатору пользователя и имени")
    void shouldReturnEmptyOptionalIfWorkoutTypeNotFoundByUserIdAndName() {
        int userId = 2;
        String type = "Nonexistent Type";

        Optional<WorkoutType> foundTypeOptional = workoutTypeDao.findTypeByUserId(userId, type);
        assertThat(foundTypeOptional).isEmpty();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
