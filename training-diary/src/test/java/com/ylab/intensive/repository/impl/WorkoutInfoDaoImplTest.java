package com.ylab.intensive.repository.impl;

import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.repository.container.PostgresTestContainer;
import com.ylab.intensive.repository.container.TestConfigurationEnvironment;
import com.ylab.intensive.repository.extractor.WorkoutInfoExtractor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты для реализации WorkoutInfoDaoImpl")
public class WorkoutInfoDaoImplTest extends TestConfigurationEnvironment {

    private static WorkoutInfoDaoImpl workoutInfoDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        JdbcTemplate jdbcTemplate = PostgresTestContainer.getJdbcTemplate();
        workoutInfoDao = new WorkoutInfoDaoImpl(jdbcTemplate, new WorkoutInfoExtractor());
    }

    @Test
    @DisplayName("Должен сохранять информацию о тренировке в базе данных")
    void shouldSaveWorkoutInfo() {
        int workoutId = 1;
        String title = "Test Title";
        String info = "Test Info";

        workoutInfoDao.saveWorkoutInfo(workoutId, title, info);

        Optional<WorkoutInfo> savedWorkoutInfoOptional = workoutInfoDao.findByWorkoutId(workoutId);
        assertThat(savedWorkoutInfoOptional).isPresent();
        WorkoutInfo savedWorkoutInfo = savedWorkoutInfoOptional.get();
        assertTrue(savedWorkoutInfo.getWorkoutInfo().containsKey(title));
        assertThat(savedWorkoutInfo.getWorkoutInfo().get(title)).isEqualTo(info);
    }

    @Test
    @DisplayName("Должен обновлять информацию о тренировке в базе данных")
    void shouldUpdateWorkoutInfo() {
        int workoutId = 1;
        String title = "distance traveled";
        String newInfo = "New Test Info";

        workoutInfoDao.updateWorkoutInfo(workoutId, title, newInfo);

        Optional<WorkoutInfo> updatedWorkoutInfoOptional = workoutInfoDao.findByWorkoutId(workoutId);
        assertThat(updatedWorkoutInfoOptional).isPresent();
        WorkoutInfo updatedWorkoutInfo = updatedWorkoutInfoOptional.get();
        assertTrue(updatedWorkoutInfo.getWorkoutInfo().containsKey(title));
        assertThat(updatedWorkoutInfo.getWorkoutInfo().get(title)).isEqualTo(newInfo);
    }

    @Test
    @DisplayName("Не должен обновлять информацию о тренировке если заголовок не найден")
    void shouldThrowDaoExceptionOnUpdate() {
        int workoutId = 1;
        String title = "Nonexistent Title";
        String newInfo = "New Test Info";

        assertThrows(DaoException.class, () -> workoutInfoDao.updateWorkoutInfo(workoutId, title, newInfo));
    }

    @Test
    @DisplayName("Должен находить информацию о тренировке по идентификатору тренировки")
    void shouldFindWorkoutInfoByWorkoutId() {
       Optional<WorkoutInfo> foundWorkoutInfoOptional = workoutInfoDao.findByWorkoutId(1);
        assertThat(foundWorkoutInfoOptional).isPresent();
    }

    @Test
    @DisplayName("Должен удалять информацию о тренировке из базы данных")
    void shouldDeleteWorkoutInfo() {
        int workoutId = 2;

        workoutInfoDao.delete(workoutId);

        Optional<WorkoutInfo> deletedWorkoutInfoOptional = workoutInfoDao.findByWorkoutId(workoutId);
        assertThat(deletedWorkoutInfoOptional).isEmpty();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
