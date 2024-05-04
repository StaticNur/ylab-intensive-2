/*
package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.container.PostgresTestContainer;
import com.ylab.intensive.dao.container.TestConfigurationEnvironment;
import com.ylab.intensive.model.entity.WorkoutType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WorkoutType Database Operations Testing")
class WorkoutTypeDaoImplTest extends TestConfigurationEnvironment {

    private static WorkoutTypeDaoImpl workoutTypeDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        workoutTypeDao = new WorkoutTypeDaoImpl();
    }

    @Test
    @DisplayName("Save workout type - success")
    void testSaveType_Success() {
        int userId = 1;
        String expectedWorkoutType = "cardio12";

        workoutTypeDao.saveType(userId, expectedWorkoutType);

        List<WorkoutType> savedWorkoutTypes = workoutTypeDao.findByUserId(userId);
        assertThat(savedWorkoutTypes)
                .extracting(WorkoutType::getType)
                .contains(expectedWorkoutType);
    }

    @Test
    @DisplayName("Update workout type - success")
    void testUpdateType_Success() {
        int userId = 1;
        String oldWorkoutType = "yoga";
        String newWorkoutType = "Strength";

        workoutTypeDao.updateType(userId, oldWorkoutType, newWorkoutType);

        List<WorkoutType> updatedWorkoutTypes = workoutTypeDao.findByUserId(userId);
        assertThat(updatedWorkoutTypes)
                .extracting(WorkoutType::getType)
                .contains(newWorkoutType);
    }

    @Test
    @DisplayName("Find types by workoutId")
    void testFindByWorkoutId() {
        int userId = 1;

        List<WorkoutType> types = workoutTypeDao.findByUserId(userId);

        assertThat(types).isNotEmpty();
    }

    @Test
    @DisplayName("Delete workout type - success")
    void testDelete_Success() {
        int userId = 2;

        workoutTypeDao.delete(userId);

        List<WorkoutType> types = workoutTypeDao.findByUserId(userId);

        assertThat(types).isEmpty();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
*/
