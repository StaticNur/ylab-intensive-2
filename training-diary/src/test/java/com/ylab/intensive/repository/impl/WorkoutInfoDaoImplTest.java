package com.ylab.intensive.repository.impl;

import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.repository.WorkoutInfoDao;
import com.ylab.intensive.repository.config.TestRepositories;
import com.ylab.intensive.tag.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@IntegrationTest
@ContextConfiguration(classes = TestRepositories.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Тесты для реализации WorkoutInfoDao")
class WorkoutInfoDaoImplTest {

    @Autowired
    private WorkoutInfoDao workoutInfoDao;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Должен сохранять информацию о тренировке в базе данных")
        void shouldSaveWorkoutInfo() {
            int workoutId = 1;
            String title = "Test Title";
            String info = "Test Info";

            workoutInfoDao.saveWorkoutInfo(workoutId, title, info);

            assertThat(workoutInfoDao.findByWorkoutId(workoutId))
                    .isPresent()
                    .hasValueSatisfying(savedWorkoutInfo -> {
                        assertTrue(savedWorkoutInfo.getWorkoutInfo()
                                .containsKey(title));
                        assertThat(savedWorkoutInfo.getWorkoutInfo().get(title))
                                .isEqualTo(info);
                    });
        }

        @Test
        @DisplayName("Должен обновлять информацию о тренировке в базе данных")
        void shouldUpdateWorkoutInfo() {
            int workoutId = 1;
            String title = "distance traveled";
            String newInfo = "New Test Info";

            workoutInfoDao.updateWorkoutInfo(workoutId, title, newInfo);

            assertThat(workoutInfoDao.findByWorkoutId(workoutId))
                    .isPresent()
                    .hasValueSatisfying(updatedWorkoutInfo -> {
                        assertTrue(updatedWorkoutInfo.getWorkoutInfo()
                                .containsKey(title));
                        assertThat(updatedWorkoutInfo.getWorkoutInfo().get(title))
                                .isEqualTo(newInfo);
                    });
        }

        @Test
        @DisplayName("Должен находить информацию о тренировке по идентификатору тренировки")
        void shouldFindWorkoutInfoByWorkoutId() {
            assertThat(workoutInfoDao.findByWorkoutId(1))
                    .isPresent();
        }

        @Test
        @DisplayName("Должен удалять информацию о тренировке из базы данных")
        void shouldDeleteWorkoutInfo() {
            int workoutId = 2;

            workoutInfoDao.delete(workoutId);

            assertThat(workoutInfoDao.findByWorkoutId(workoutId))
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Не должен обновлять информацию о тренировке если заголовок не найден")
        void shouldThrowDaoExceptionOnUpdate() {
            int workoutId = 1;
            String title = "Nonexistent Title";
            String newInfo = "New Test Info";

            assertThrows(DaoException.class, () -> workoutInfoDao.updateWorkoutInfo(workoutId, title, newInfo));
        }
    }
}