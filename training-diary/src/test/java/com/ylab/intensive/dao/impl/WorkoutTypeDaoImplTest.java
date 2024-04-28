/*
package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.container.PostgresTestContainer;
import com.ylab.intensive.dao.container.TestConfigurationEnvironment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

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
        int workoutId = 1;
        String type = "cardio";

        workoutTypeDao.saveType(workoutId, type);

        Set<String> types = workoutTypeDao.findByUserId(workoutId);

        assertThat(types)
                .isNotEmpty()
                .contains(type);
    }

    @Test
    @DisplayName("Update workout type - success")
    void testUpdateType_Success() {
        int workoutId = 1;
        String oldType = "yoga";
        String newType = "Strength";

        workoutTypeDao.updateType(workoutId, oldType, newType);

        Set<String> types = workoutTypeDao.findByUserId(workoutId);

        assertThat(types)
                .isNotEmpty()
                .contains(newType)
                .doesNotContain(oldType);
    }

    @Test
    @DisplayName("Find types by workoutId")
    void testFindByWorkoutId() {
        int workoutId = 1;

        Set<String> types = workoutTypeDao.findByUserId(workoutId);

        assertThat(types).isNotEmpty();
    }

    @Test
    @DisplayName("Delete workout type - success")
    void testDelete_Success() {
        int workoutId = 2;

        workoutTypeDao.delete(workoutId);

        Set<String> types = workoutTypeDao.findByUserId(workoutId);

        assertThat(types).isEmpty();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
*/
