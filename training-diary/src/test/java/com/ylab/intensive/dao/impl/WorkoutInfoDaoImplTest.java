package com.ylab.intensive.dao.impl;

import com.ylab.intensive.dao.container.PostgresTestContainer;
import com.ylab.intensive.dao.container.TestConfigurationEnvironment;
import com.ylab.intensive.model.entity.WorkoutInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WorkoutInfo Database Operations Testing")
class WorkoutInfoDaoImplTest extends TestConfigurationEnvironment {

    private static WorkoutInfoDaoImpl workoutInfoDao;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = PostgresTestContainer.getInstance();
        workoutInfoDao = new WorkoutInfoDaoImpl();
    }

    @Test
    @DisplayName("Save workout info - success")
    void testSaveWorkoutInfo_Success() {
        int workoutId = 1;
        String title = "Title";
        String info = "Info";

        workoutInfoDao.saveWorkoutInfo(workoutId, title, info);

        WorkoutInfo workoutInfoMap = workoutInfoDao.findByWorkoutId(workoutId);

        assertThat(workoutInfoMap.getWorkoutInfo())
                .isNotEmpty()
                .containsEntry(title, info);
    }

    @Test
    @DisplayName("Update workout info - success")
    void testUpdateWorkoutInfo_Success() {
        int workoutId = 1;
        String title = "Title";
        String newInfo = "New Info";

        workoutInfoDao.updateWorkoutInfo(workoutId, title, newInfo);

        WorkoutInfo workoutInfoMap = workoutInfoDao.findByWorkoutId(workoutId);

        assertThat(workoutInfoMap.getWorkoutInfo())
                .isNotEmpty()
                .containsEntry(title, newInfo);
    }

    @Test
    @DisplayName("Find workout info by workoutId")
    void testFindByWorkoutId() {
        int workoutId = 1;

        WorkoutInfo workoutInfoMap = workoutInfoDao.findByWorkoutId(workoutId);

        assertThat(workoutInfoMap.getWorkoutInfo()).isNotEmpty();
    }

    @Test
    @DisplayName("Delete workout info - success")
    void testDelete_Success() {
        int workoutId = 2;

        workoutInfoDao.delete(workoutId);

        WorkoutInfo workoutInfoMap = workoutInfoDao.findByWorkoutId(workoutId);

        assertThat(workoutInfoMap.getWorkoutInfo()).isEmpty();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.stop();
    }
}
