package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutInfoDao;
import com.ylab.intensive.model.entity.WorkoutInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Workout Info Service Tests")
public class WorkoutInfoServiceImplTest {

    @Mock
    private WorkoutInfoDao workoutInfoDao;

    @InjectMocks
    private WorkoutInfoServiceImpl workoutInfoService;

    @Test
    @DisplayName("Save workout info - success")
    void testSaveWorkoutInfo_Success() {
        int workoutId = 1;
        String title = "Title";
        String info = "Info";

        workoutInfoService.saveWorkoutInfo(workoutId, title, info);

        verify(workoutInfoDao).saveWorkoutInfo(workoutId, title, info);
    }

    @Test
    @DisplayName("Update workout info - success")
    void testUpdateWorkoutInfo_Success() {
        int workoutId = 1;
        String title = "Title";
        String info = "Updated Info";

        workoutInfoService.updateWorkoutInfo(workoutId, title, info);

        verify(workoutInfoDao).updateWorkoutInfo(workoutId, title, info);
    }

    @Test
    @DisplayName("Get info by workout ID - success")
    void testGetInfoByWorkoutId_Success() {
        int workoutId = 1;
        WorkoutInfo expectedInfo = new WorkoutInfo(10, 2, new HashMap<>());
        expectedInfo.getWorkoutInfo().put("Title", "Info");

        when(workoutInfoDao.findByWorkoutId(workoutId)).thenReturn(expectedInfo);

        WorkoutInfo result = workoutInfoService.getInfoByWorkoutId(workoutId);

        assertThat(result).isEqualTo(expectedInfo);
    }

    @Test
    @DisplayName("Delete workout info - success")
    void testDelete_Success() {
        int workoutId = 1;

        workoutInfoService.delete(workoutId);

        verify(workoutInfoDao).delete(workoutId);
    }
}
