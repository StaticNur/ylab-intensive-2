package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutTypeDao;
import com.ylab.intensive.model.entity.WorkoutType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Workout Type Service Tests")
public class WorkoutTypeServiceImplTest {

    @Mock
    private WorkoutTypeDao workoutTypeDao;

    @InjectMocks
    private WorkoutTypeServiceImpl workoutTypeService;

    @Test
    @DisplayName("Save workout type - success")
    void testSaveType_Success() {
        int workoutId = 1;
        String typeName = "Cardio";

        workoutTypeService.saveType(workoutId, typeName);

        verify(workoutTypeDao).saveType(workoutId, typeName);
    }

    @Test
    @DisplayName("Find types by workout ID - success")
    void testFindByWorkoutId_Success() {
        int workoutId = 1;
        List<WorkoutType> expectedTypes = new ArrayList<>();
        expectedTypes.add(new WorkoutType());
        expectedTypes.add(new WorkoutType());

        when(workoutTypeDao.findByUserId(workoutId)).thenReturn(expectedTypes);

        List<WorkoutType> result = workoutTypeService.findByUserId(workoutId);

        assertThat(result).isEqualTo(expectedTypes);
    }

    @Test
    @DisplayName("Update workout type - success")
    void testUpdateType_Success() {
        int workoutId = 1;
        String oldType = "Cardio";
        String newType = "Yoga";

        workoutTypeService.updateType(workoutId, oldType, newType);

        verify(workoutTypeDao).updateType(workoutId, oldType, newType);
    }

    @Test
    @DisplayName("Delete workout types - success")
    void testDelete_Success() {
        int userId = 1;

        workoutTypeService.delete(userId);

        verify(workoutTypeDao).delete(userId);
    }
}
